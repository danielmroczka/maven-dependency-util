/*
 * FileSize.java 
 * (C) Copyright daniel.mroczka@gmail.com 2008. All rights reserved. 
 * Author: Daniel Mroczka  Creation date: 2009-04-05, 13:02:36
 */
package com.dm.mavenplugin.mojos;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

/**
 * Goal which counting final size of all dependencies in classpath.
 *
 * @goal summary-size
 * @phase process-sources
 */
public class FileSize extends AbstractMojo {

    private Log log;
    /**
     * @parameter expression="${message}"
     */
    private String message;
    /**
     * @parameter expression="${active}"
     */
    private boolean active = true;
    /**
     * Location of the file.
     *
     * @parameter expression="${project.build.directory}"
     * @required
     */
    private File outputDirectory;
    /**
     * @parameter default-value="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;
    /**
     * @parameter default-value="${plugin.artifacts}"
     */
    private List pluginArtifacts;
    /**
     * @component
     */
    private ArtifactFactory artifactFactory;
    /**
     * @component
     */
    private ArtifactResolver resolver;
    /**
     * @parameter default-value="${localRepository}"
     */
    private ArtifactRepository localRepository;
    /**
     * @parameter default-value="${project.remoteArtifactRepositories}"
     */
    private List remoteRepositories;
    /**
     * @parameter default-value="${project.distributionManagementArtifactRepository}"
     */
    private ArtifactRepository deploymentRepository;

    @Override
    public void setLog(Log log) {
        this.log = log;
    }

    @Override
    public Log getLog() {
        return log;
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (active) {
            File file = new File(project.getBuild().getDirectory() + File.separator + project.getBuild().getFinalName() + ".jar");
            log.info(message);
            log.info("Artifact size: " + file.length() + " bytes");
            log.info("Used depenedencies: ");
            String baseDir = localRepository.getBasedir() ;
            int sum = 0;
            int count = 0;
            for (Iterator<Dependency> it = project.getDependencies().iterator(); it.hasNext();) {
                
                Dependency d = it.next();
                if (!"compile".equals(d.getScope())) {
                    continue;
                }
                String[] group = d.getGroupId().split("\\.");
                String[] artifact = d.getArtifactId().split("\\.");
                String path = baseDir;
                for (String g:group) {
                    path += File.separator + g;
                }
                for (String a:artifact) {
                    path += File.separator + a;
                }
                path+= File.separator + d.getVersion();
                path += File.separator + d.getArtifactId() + "-" + d.getVersion() + "." + d.getType();
                File f = new File(path);
                sum += f.length();
                log.info("Size: " +f.length() + "\t" + d);
                count++;
            }
            log.info("Summary Report:");
            log.info("COMPILE SCOPE");
            log.info("Summary dependencies count=" + count);
            log.info("Summary dependencies size=" + sum);
            try {
                log.info(""+ project.getCompileClasspathElements().size());
                log.info("" + project.getCompileArtifacts().size());
                log.info("" + project.getCompileDependencies().size());
                log.info("" + project.getCompileSourceRoots().size());
                log.info("" + project.getCompileArtifacts().size());
                log.info("" + project.getArtifacts().size());
            } catch (DependencyResolutionRequiredException ex) {
                Logger.getLogger(FileSize.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
