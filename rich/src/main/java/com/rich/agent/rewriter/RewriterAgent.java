package com.rich.agent.rewriter;

import java.lang.instrument.Instrumentation;

public class RewriterAgent {

	public static void agentmain(String agentArgs,
			Instrumentation instrumentation) {
		premain(agentArgs, instrumentation);
	}

	public static void premain(String agentArgs,
			Instrumentation instrumentation) {
		instrumentation.addTransformer(new RichClassFileTransFormat());
	}

}
