package qslv.util;

import java.util.HashMap;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExternalResourceSLIAspect implements BeanFactoryAware {
	private Logger log = null;
	StandardEvaluationContext context;
	private ExpressionParser parser = new SpelExpressionParser();
	private TemplateParserContext tcontext = new TemplateParserContext();
	HashMap<String, String> resolvedLiterals = new HashMap<>();

	@Around("@annotation(externalResourceSLI)")
	public Object remoteService(ProceedingJoinPoint joinPoint, ExternalResourceSLI externalResourceSLI) throws Throwable {
		if ( null == log ) {
			if (externalResourceSLI.logScope().equals(ExternalResourceSLI.class)) {
				log = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
			} else {
				log = LoggerFactory.getLogger(externalResourceSLI.logScope());
			}
		}
		String ait = resolvedLiterals.computeIfAbsent(externalResourceSLI.ait(), this::resolveLiteral);
		long start = System.nanoTime();
		Object proceed=null;
		try {
			proceed = joinPoint.proceed();
		} catch (Throwable thrown) {
			for( Class<? extends Throwable> element : externalResourceSLI.remoteFailures() ) {
				if (thrown.getClass().isAssignableFrom(element) ) {
					ServiceLevelIndicator.logServiceFailureElapsedTime(log, externalResourceSLI.value(), ait, (System.nanoTime() - start) );
				}
			}
			throw thrown;
		}
		ServiceLevelIndicator.logServiceElapsedTime(log, externalResourceSLI.value(), ait, (System.nanoTime() - start) );
		return proceed;
	}

	private String resolveLiteral( String literal ) {
		String resolved;
		try {
			resolved = parser.parseExpression(literal,tcontext).getValue(context, String.class);
		} catch (Throwable thrown ) {
			resolved = literal;
			log.error("Parse error for \"{}\" {}", literal, thrown.getMessage());
		}
		return resolved;
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		context = new StandardEvaluationContext(beanFactory);
		context.setBeanResolver(new BeanFactoryResolver(beanFactory));
	}

}
