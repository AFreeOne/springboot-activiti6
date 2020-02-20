package org.freeatalk.freeone.springboot.activiti6.aop;


import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

//import lombok.Data;
//import lombok.extern.slf4j.Slf4j;



/**
 * 控制台输出 打印需要的信息,支持快速定位到具体的文件
 * https://blog.csdn.net/mengxiangxingdong/article/details/82193781
 */
@Aspect
@Component
public class LogExtAopNew {
	
	
	private static final Logger log = LoggerFactory.getLogger(LogExtAopNew.class);

	
    @Pointcut(value = "@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void cutLogService() {
    }

    @Around("cutLogService()")
    public Object recordSysLog(ProceedingJoinPoint point) throws Throwable {
        boolean logOut = true;
		if (logOut ) { // 开启日志
            // 记录请求执行时间
            long start = System.currentTimeMillis();
            Object result = point.proceed();
            long end = System.currentTimeMillis();
            try {
                HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
                MethodSignature signature = (MethodSignature) point.getSignature();
                Method method = signature.getMethod();
                Annotation[] annotations = method.getAnnotations();
                LogExtModelNew logExtModel = new LogExtModelNew(method);
                logExtModel.setMethodArgs(point.getArgs());
                logExtModel.setResult(result);
                logExtModel.setUrl(request.getRequestURL().toString()); // 获得方法进来的url
                logExtModel.setIp(request.getRemoteAddr()); // 获得方法进来的ip
                logExtModel.setExecuteTime(LogExtUtil.getTime(start, end)); // 方法的执行时间
                logExtModel.setAnnotations(annotations);
                String submitMehtod = request.getMethod().toUpperCase();
                logExtModel.setType(submitMehtod);
                logExtModel.setRequestArgs(LogExtUtil.getRequestQueryString(request));
                logExtModel.setParamtersString(LogExtUtil.getRequestParamtersString(request));
//                System.out.println(logExtModel.toString()  );
                log.info(logExtModel.toString());
                
            } catch (Exception e) {
            	e.printStackTrace();
                System.err.println("日志记录出错!");
            }
            return result;
        } else {
            return point.proceed(); // 否则什么都不管
        }
    }
}
//@Data
final class LogExtModelNew {
    private String url; // 访问的url
    private String type ;
    private String requestArgs; // 请求的参数
    private String ip; // 到指定的ip
    private Object result; // 返回到前台的数据
    private Object[] methodArgs; // 方法的参数
    private String executeTime; // 方法执行时间
    private Annotation[] annotations; // 方法上的注解
    // ====================参考 StackTraceElement 对象
    private String declaringClass;
    private String methodName;
    private String fileName;
    private int lineNumber;
    private String ParamtersString ;
    
    
    
    public String getParamtersString() {
		return ParamtersString;
	}

	public void setParamtersString(String paramtersString) {
		ParamtersString = paramtersString;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public LogExtModelNew(Method method) {
        this.declaringClass = method.getDeclaringClass().getName();
        this.methodName = method.getName();
        this.fileName = method.getDeclaringClass().getSimpleName() + ".java";
        this.lineNumber = 1;
    }

    public String getRequestArgs() {
		return requestArgs;
	}

	public void setRequestArgs(String requestArgs) {
		this.requestArgs = requestArgs;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public Object[] getMethodArgs() {
		return methodArgs;
	}

	public void setMethodArgs(Object[] methodArgs) {
		this.methodArgs = methodArgs;
	}

	public String getExecuteTime() {
		return executeTime;
	}

	public void setExecuteTime(String executeTime) {
		this.executeTime = executeTime;
	}

	public Annotation[] getAnnotations() {
		return annotations;
	}

	public void setAnnotations(Annotation[] annotations) {
		this.annotations = annotations;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	// ====================参考 StackTraceElement 对象
    public String getUrlMaping() { // 获得映射的地址
    	List<String> methodArgsString = new ArrayList<String>();
    	for (int i = 0; i < methodArgs.length; i++) {
			if (methodArgs[i] != null) {
				methodArgsString.add(methodArgs[i].toString());
			}
		}
    	
    	
        return declaringClass + "." + methodName + "(" +String.join(", ", methodArgsString) + ")";
    }

    public String getUrl() {
        return this.url + "   "
                + (isNativeMethod() ? "(Native Method)"
                        : (fileName != null && lineNumber >= 0 ? "(" + fileName + ":" + lineNumber + ")"
                                : (fileName != null ? "(" + fileName + ")" : "(Unknown Source)")));
    }

    @Override
    public String toString() {
        // 扩展模式控制扩展打印 基础模式控制基础
        String nextLine =  "\r\n";
        String msg =   nextLine;
        boolean baseFlag = true ;
        // 基础日志记录
        if (baseFlag) {
        	msg += "---------------------------------------------------------------------------------------------------------------------------------------------------" + nextLine;
        	msg += "	@时间 			：	" + LogExtUtil.getNowTime() + nextLine;
            msg += "	@请求url		 	：	" +this.getType() + "	"+ this.getUrl() + "点击左侧 () 可快速定位到文件" + nextLine;
            msg += "	@映射类.方法(参数值) 	：	" + this.getUrlMaping() + nextLine;
            msg += "	@参数 			：	" + this.getParamtersString() + nextLine;
            msg += "	@方法返回值result	:	" + this.getResult() + nextLine;
            if (!Arrays.toString(this.annotations).contains("@org.springframework.web.bind.annotation.ResponseBody")) {
                if (this.result != null && this.result instanceof String) {
                    msg += "	@项目磁盘路径		：	" + LogExtUtil.getProjectPath() + nextLine;
                    msg += "	@跳转的页面		：	" + this.getView() + nextLine ;
                }
            }
            msg += "---------------------------------------------------------------------------------------------------------------------------------------------------" ;
        }
        // 扩展日志记录
        boolean extFlag = false ;
        if (extFlag) {
            msg += "扩展日志 ：" + nextLine;
            msg += "	@用户ip			：	" + this.getIp() + nextLine;
            msg += "	@请求方法执行时间	：	" + this.executeTime;
        }
        return msg;
    }

    private String getView() { // 检查返回值 如果符合一定的规则 那么补充对应的页面的后缀
        String suffix =	 "" ;
        String result = "";
        if (this.result instanceof ModelAndView) { // 证明这里从ModelAndView 里面获取
            result = ((ModelAndView) this.result).getViewName();
        } else {
        	if (this.result == null) {
				result = "";
			}else {
				result = this.result.toString();
			}
//            result = result == "" ? this.result.toString() : result;
        }
        return "" + result + suffix + ""; // 跳转的页面
    }

    public boolean isNativeMethod() {
        return lineNumber == -2;
    }

}

final class LogExtUtil {
	
    protected static final int DATA_COUNT_NUMER = 10 * 10000; // 每10w数据记录一次
    protected static final int SECOND = 1 * 1000; // 秒
    protected static final int MINUTE = 60 * 1000; // 分钟
    protected static final int HOUR = 60 * 60 * 1000; // 小时

    /**
     * 根据一定的规则展示合适的时间 / 1秒内 用 毫秒 1分钟内用秒 1小时内用分钟 2小时外 用小时
     * 
     * @Title getTime
     * @author 于国帅
     * @date 2018年7月11日 下午4:46:12
     * @param start
     * @param end
     * @return String
     */
    public static String getTime(long start, long end) {
        end = end - start;
        if (end < SECOND) {
            return end + "毫秒";
        } else if (end < MINUTE) {
            return end / SECOND + "秒";
        } else if (end < HOUR) { // * 2
            return end / MINUTE + "分" + (end % MINUTE) / SECOND + "秒";
        } else {
            return end / HOUR + "小时" + (end % HOUR) / MINUTE + "分";
        }
    }

    /**
     * 描述:获取 request 中请求的内容
     * 
     * @param request
     * @return
     * @throws IOException
     */
    public static String getRequestQueryString(HttpServletRequest request) throws IOException {
        String submitMehtod = request.getMethod();
        // GET
        if (submitMehtod.equals("GET")) {
            submitMehtod = request.getQueryString();
            return submitMehtod == null ? null : URLDecoder.decode(submitMehtod, "utf-8");
        } else {
            return getRequestPostStr(request);
        }
        
    }

    /***
     * 获取 request 中 json 字符串的内容
     * 
     * @param request
     * @return : <code>byte[]</code>
     * @throws IOException
     */
    public static String getRequestJsonString(HttpServletRequest request) throws IOException {
        String submitMehtod = request.getMethod();
        // GET
        if (submitMehtod.equals("GET")) {
            submitMehtod = request.getQueryString();
            return submitMehtod == null ? null
                    : new String(request.getQueryString().getBytes("iso-8859-1"), "utf-8").replaceAll("%22", "\"");
            // POST
        } else {
            return getRequestPostStr(request);
        }
    }

    /**       
     * 描述:获取 post 请求的 byte[] 数组 
     * @param request 
     * @return 
     * @throws IOException       
     */  
    public static byte[] getRequestPostBytes(HttpServletRequest request)  
            throws IOException {  
        int contentLength = request.getContentLength();  
        if(contentLength<0){  
            return null;  
        }  
        byte buffer[] = new byte[contentLength];  
        for (int i = 0; i < contentLength;) {  
            int readlen = request.getInputStream().read(buffer, i,    contentLength - i);  
            if (readlen == -1) {  
                break;  
            }  
            i += readlen;  
        }  
        return buffer;  
    }  

    /**       
     * 描述:获取 post 请求内容 
     * @param request 
     * @return 
     * @throws IOException       
     */  
    public static String getRequestPostStr(HttpServletRequest request)   throws IOException {  
        byte buffer[] = getRequestPostBytes(request);  
        String charEncoding = request.getCharacterEncoding();  
        if (charEncoding == null) {  
            charEncoding = "UTF-8";  
        }  
        String string = new String(buffer, charEncoding);
        return string;  
    }  
    /**
     * 描述:获取所有传递的参数字符串
     * @param request
     * @return
     */
    public static String getRequestParamtersString(HttpServletRequest request) {
    	  String ParamtersString = "" ;
    	  Map<String, String[]> properties = request.getParameterMap();//把请求参数封装到Map<String, String[]>中
          String name = "";
          String value = "";
          for (Map.Entry<String, String[]> entry : properties.entrySet()) {
             name = entry.getKey();
              String[] values = entry.getValue();
              if (null == values) {
                  value = "";
              } else if (values.length>1) {
                  for (int i = 0; i < values.length; i++) { //用于请求参数中有多个相同名称
                      value = values[i] + ",";
                  }
                  value = value.substring(0, value.length() - 1);
              } else {
                  value = values[0];//用于请求参数中请求参数名唯一
              }
              ParamtersString += name+ " = " + value + " , "; 
          }
          return ParamtersString ;
    }
    public static String getProjectPath() {
        return getSystemProperty("user.dir");
    }
    
    public static String getNowTime() {
    	SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日  HH时mm分ss秒");
		return format.format(new Date());
    }

    private static String getSystemProperty(String property) {
        try {
            return System.getProperty(property);
        } catch (SecurityException ex) {
            // we are not allowed to look at this property
        	ex.printStackTrace();
            System.err.println("Caught a SecurityException reading the system property '" + property
                    + "'; the SystemUtils property value will default to null.");
            return null;
        }
    }
    
  
}