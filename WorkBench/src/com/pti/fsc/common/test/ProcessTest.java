package com.pti.fsc.common.test;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.TaskSummary;
import org.kie.services.client.api.RemoteRestRuntimeEngineFactory;
import org.kie.services.client.api.RemoteRestRuntimeFactory;

public class ProcessTest {
	
	// start a new process instance
	
	public String oneEvaluationProcess() throws Exception {
		try {
			System.out.println("==========oneEvaluationProcess");
			//RemoteRestRuntimeFactory restSessionFactory = new RemoteRestRuntimeFactory("org.jbpm:Evaluation:1.0", new URL("http://localhost:9086/kie-wb"), "admin", "admin");
			
			RemoteRestRuntimeEngineFactory restSessionFactory = RemoteRestRuntimeEngineFactory.newBuilder()
					.addDeploymentId("org.jbpm:Evaluation:1.0")
                    .addUserName("admin").addPassword("admin")
                    .addUrl(new URL("http://localhost:9088/kie-wb")).build();
			
			System.out.println("==========restSessionFactory" + restSessionFactory);
			RuntimeEngine engine = restSessionFactory.newRuntimeEngine();
			System.out.println("==========restSessionFactory" + engine);
			KieSession ksession = engine.getKieSession();
			System.out.println("==========restSessionFactory" + ksession);
			TaskService taskService = engine.getTaskService();
			System.out.println("==========restSessionFactory" + taskService);
			// start a new process instance
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("employee", "SYSADM:SYSADM:ADMINISTRATOR");
			params.put("reason", "Yearly performance evaluation");
			System.out.println("==========started");
			try {
				ProcessInstance processInstance = ksession.startProcess("evaluation", params);
				System.out.println("==========getProcessId:" + processInstance.getProcessId());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("==========" +e.getMessage());
				e.printStackTrace();
			} catch (Throwable e) {
				System.out.println("==========" +e.getMessage());
				e.printStackTrace();
			}
			System.out.println("Process started ...");
		} catch (Exception e) {
			System.out.println("==========" +e.getMessage());
			e.printStackTrace();
		}
		return "Process started ...";
	}
	
	// complete Self Evaluation
	public String twoEvaluationProcess() throws Exception {
		TaskSummary task;
		try {
			RemoteRestRuntimeFactory restSessionFactory = new RemoteRestRuntimeFactory("org.jbpm:Evaluation:1.0", new URL("http://localhost:9086/kie-wb"), "krisv", "krisv");
			RuntimeEngine engine = restSessionFactory.newRuntimeEngine();
			TaskService taskService = engine.getTaskService();
			// complete Self Evaluation
			List<TaskSummary> tasks = taskService.getTasksAssignedAsPotentialOwner("krisv", "en-UK");
			task = tasks.get(0);
			System.out.println("'krisv' completing task " + task.getName() + ": " + task.getDescription());
			taskService.start(task.getId(), "krisv");
			Map<String, Object> results = new HashMap<String, Object>();
			results.put("performance", "exceeding");
			taskService.complete(task.getId(), "krisv", results);
			return "'krisv' completing task " + task.getName() + ": " + task.getDescription();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	// john from HR
	public String threeEvaluationProcess() throws Exception {
		RemoteRestRuntimeFactory restSessionFactory = new RemoteRestRuntimeFactory("org.jbpm:Evaluation:1.0", new URL("http://localhost:9086/kie-wb"), "john", "john");
		RuntimeEngine engine = restSessionFactory.newRuntimeEngine();
		TaskService taskService = engine.getTaskService();
		Map<String, Object> results = new HashMap<String, Object>();
		List<TaskSummary> tasks = taskService.getTasksAssignedAsPotentialOwner("john", "en-UK");
		TaskSummary task = tasks.get(0);
		System.out.println("'john' completing task " + task.getName() + ": " + task.getDescription());
		taskService.claim(task.getId(), "john");
		taskService.start(task.getId(), "john");
		results = new HashMap<String, Object>();
		results.put("performance", "acceptable");
		taskService.complete(task.getId(), "john", results);
		return "'john' completing task " + task.getName() + ": " + task.getDescription();
	}
	
	// mary from PM
	public String foreEvaluationProcess() throws Exception {
		RemoteRestRuntimeFactory restSessionFactory = new RemoteRestRuntimeFactory("org.jbpm:Evaluation:1.0", new URL("http://localhost:9086/kie-wb"), "mary", "mary");
		RuntimeEngine engine = restSessionFactory.newRuntimeEngine();
		TaskService taskService = engine.getTaskService();
		Map<String, Object> results = new HashMap<String, Object>();
		
		List<TaskSummary> tasks = taskService.getTasksAssignedAsPotentialOwner("mary", "en-UK");
		TaskSummary task = tasks.get(0);
		System.out.println("'mary' completing task " + task.getName() + ": " + task.getDescription());
		taskService.claim(task.getId(), "mary");
		taskService.start(task.getId(), "mary");
		results = new HashMap<String, Object>();
		results.put("performance", "outstanding");
		taskService.complete(task.getId(), "mary", results);
		return "'mary' completing task " + task.getName() + ": " + task.getDescription();
	}
	
	public static void main(String[] args) {
		ProcessTest test = new ProcessTest();
		try {
			test.oneEvaluationProcess();
			
			//test.twoEvaluationProcess();
			
			//test.threeEvaluationProcess();
			
			//test.foreEvaluationProcess();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
