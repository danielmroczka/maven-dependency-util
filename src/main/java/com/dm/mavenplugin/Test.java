/*
 * Copyright daniel.mroczka@gmail.com. All rights reserved. 
 */
package com.dm.mavenplugin;

import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.dependency.tree.DefaultDependencyTreeBuilder;
import org.apache.maven.shared.dependency.tree.DependencyTreeBuilder;

/**
 * @author daniel
 */
public class Test {
    public void execute() {
        DependencyTreeBuilder p = new DefaultDependencyTreeBuilder();
        MavenProject pp;
    }
}
