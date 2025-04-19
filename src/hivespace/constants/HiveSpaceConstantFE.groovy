package org.constants

public class HiveSpaceConstantFE {
    
    static HiveSpaceProject project = new HiveSpaceProject(
        name: 'HiveSpace frontend',
        gitRepo: 'https://github.com/HiveSpaceTeam/hivespace.frontend',
        branch: 'master',
        credentialsId: 'dockerhub-credentials',
        apps: [
            new HiveSpaceApp(name: 'frontend', dockerImage: 'dblmint/hivespace-frontend'),
        ]
    )

}
