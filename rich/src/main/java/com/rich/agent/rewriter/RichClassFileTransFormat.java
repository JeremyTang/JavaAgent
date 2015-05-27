package com.rich.agent.rewriter;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.ArrayList;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

public class RichClassFileTransFormat implements ClassFileTransformer {

	final static String codeStart = "\nlong startTime = System.currentTimeMillis();\n";
	final static String codeEnd = "\nlong endTime = System.currentTimeMillis();\n";
	final static ArrayList<String> methods = new ArrayList<String>();

	static {
		methods.add("com.rich.java.imp.AgantTest.printSomething");
		methods.add("com.rich.java.imp.AgantTest.secondSomething");
	}

	@Override
	public byte[] transform(ClassLoader loader, String className,
			Class<?> classBeingRedefined, ProtectionDomain protectionDomain,
			byte[] classfileBuffer) throws IllegalClassFormatException {
		if (!className.startsWith("com/rich/java/imp")) {
			return null;
		}
		className = className.replace("/", ".");
		CtClass ctClass = null;
		try {
			System.out.println("Before ctClass = " + className);
			ctClass = ClassPool.getDefault().getCtClass(className);
			System.out.println("After ctClass = " + className);
			for (String method : methods) {
				System.out.println("Method = " + method);
				if (method.startsWith(className)) {
					String methodName = method.substring(
							method.lastIndexOf(".") + 1, method.length());
					String outputStr = "\nSystem.out.println(\"this method "
							+ methodName
							+ " cost:\" +(endTime - startTime) +\"ms.\");";

					// 得到这方法实例
					CtMethod ctMethod = ctClass.getDeclaredMethod(methodName);
					ctMethod.insertBefore(codeStart);
					ctMethod.insertAfter(codeEnd + outputStr);
					// // 新定义一个方法叫做比如sayHello$impl
					// String newMethodName = methodName + "$impl";
					// // 原来的方法改个名字
					// ctMethod.setName(newMethodName);
					// // 创建新的方法，复制原来的方法 ，名字为原来的名字
					// CtMethod newMethod = CtMethod.copy(ctMethod, methodName,
					// ctClass, null);
					// // 构建新的方法体
					// StringBuilder bodyStr = new StringBuilder();
					// bodyStr.append("{");
					// bodyStr.append(codeStart);
					// // 调用原有代码，类似于method();($$)表示所有的参数
					// bodyStr.append(newMethodName + "($$);\n");
					//
					// bodyStr.append(codeEnd);
					// bodyStr.append(outputStr);
					//
					// bodyStr.append("}");
					// // 替换新方法
					// newMethod.setBody(bodyStr.toString());
					// // 增加新方法
					// ctClass.addMethod(newMethod);
				}
			}
			return ctClass.toBytecode();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("特么的出异常了！");
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		return null;
	}

}
