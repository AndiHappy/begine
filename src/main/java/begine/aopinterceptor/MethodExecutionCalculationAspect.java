package begine.aopinterceptor;

import org.apache.catalina.connector.RequestFacade;
import org.apache.catalina.connector.ResponseFacade;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

/**
 * 切面监测每一个方法的实现
 */
@Aspect
@Configuration
public class MethodExecutionCalculationAspect {

	private Logger logger = LoggerFactory.getLogger(MethodExecutionCalculationAspect.class);

	/**
	 * Around 最好要把返回值返回
	 * */
	@Around("execution(* begine.controller.*Controller.*(..))")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		long startTime = System.currentTimeMillis();

		Object[] arguments = joinPoint.getArgs();
		StringBuffer paramValue = new StringBuffer("");
		if (arguments != null && arguments.length > 0) {
			for (int i = 0; i < arguments.length; i++) {
				Object para = arguments[i];
				if(!(para instanceof RequestFacade) && !(para instanceof ResponseFacade)){
					paramValue.append(para + "##");
				}
			}
		}
		String para = paramValue.toString();
		String methodName = joinPoint.getSignature().toShortString();
		Object returnValue = joinPoint.proceed();
		long timeTaken = System.currentTimeMillis() - startTime;
		logger.info("Method Name:{},para:{} returnValue:{},waste:{} ms", methodName, para, returnValue==null?"void":returnValue.toString(), timeTaken);
		return returnValue;
	}
}