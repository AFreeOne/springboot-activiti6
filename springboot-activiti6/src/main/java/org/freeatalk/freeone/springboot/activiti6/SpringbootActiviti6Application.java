package org.freeatalk.freeone.springboot.activiti6;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@MapperScan({"org.freeatalk.freeone.springboot.activiti6.dao"})
public class SpringbootActiviti6Application {

	public static void main(String[] args) {
//		RepositoryService repositoryService = ProcessEngines.getDefaultProcessEngine().getRepositoryService();
//		DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
//        //设置部署名称并将两个流程定义文件添加到部署构建器中
//        deploymentBuilder.name("测试部署资源")
//                .addClasspathResource("leave.bpmn");
//        
//        //由部署构建器生成一个部署对象，即将两个定义文件部署到数据库中
//        Deployment deploy = deploymentBuilder.deploy();
		
		SpringApplication.run(SpringbootActiviti6Application.class, args);
		System.out.println("==============================DONE============================================");
	}

}
