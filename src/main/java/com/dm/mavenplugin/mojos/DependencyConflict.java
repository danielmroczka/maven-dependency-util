package com.dm.mavenplugin.mojos;

import com.dm.mavenplugin.DependencyTuple;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

/**
 * Goal which touches a timestamp file.
 *
 * @goal check-dependency
 * @phase process-sources
 */
public class DependencyConflict extends AbstractMojo {

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
    private java.util.List pluginArtifacts;
    /**
     * @component
     */
    private org.apache.maven.artifact.factory.ArtifactFactory artifactFactory;
    /**
     * @component
     */
    private org.apache.maven.artifact.resolver.ArtifactResolver resolver;
    /**
     * @parameter default-value="${localRepository}"
     */
    private org.apache.maven.artifact.repository.ArtifactRepository localRepository;
    /**
     * @parameter default-value="${project.remoteArtifactRepositories}"
     */
    private java.util.List remoteRepositories;
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
        log.info("Checking conflict dependencies...");
        List<DependencyTuple> list = conflictDetect(project.getDependencies());
        log.info(String.format("Found %d conflicts", list.size()));

        for (DependencyTuple item : list) {
            log.error("Conflict: " + item.dependency1 + " and " + item.dependency2);
        }
    }

    protected List<DependencyTuple> conflictDetect(List<Dependency> dependencies) {
        List<DependencyTuple> result = new ArrayList<DependencyTuple>();
        Collections.sort(dependencies, new MyComparator());

        String key = "";
        Dependency previous = null;

        for (Dependency dependency : dependencies) {
            System.out.println(dependency.getArtifactId());
            String temp = keyBuilder(dependency);
            if (key.equals(temp)) {
                //System.out.println("Detect!");
                result.add(new DependencyTuple(previous, dependency));
            } else {
                key = temp;
            }

            previous = dependency;
        }

        return result;

    }

    private String keyBuilder(Dependency dep) {
        return dep.getGroupId() + ":" + dep.getArtifactId();
    }

    class MyComparator implements Comparator<Dependency> {

        public int compare(Dependency o1, Dependency o2) {
            int hashCode1 = (o1.getArtifactId() + o1.getGroupId()).hashCode();
            int hashCode2 = (o2.getArtifactId() + o2.getGroupId()).hashCode();

            return hashCode2 - hashCode1;
        }
    }
}
