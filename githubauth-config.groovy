import jenkins.model.*
import hudson.security.SecurityRealm
import org.jenkinsci.plugins.GithubSecurityRealm

String githubWebUri = 'https://github.com'
String githubApiUri = 'https://api.github.com'
String clientID = System.getenv("GITHUB_JENKINS_CLIENT_ID")
String clientSecret = System.getenv("GITHUB_JENKINS_CLIENT_SECRET")
String oauthScopes = 'read:org,user:email'

SecurityRealm github_realm = new GithubSecurityRealm(githubWebUri, githubApiUri, clientID, clientSecret, oauthScopes)
//check for equality, no need to modify the runtime if no settings changed
if(!github_realm.equals(Jenkins.instance.getSecurityRealm())) {
    Jenkins.instance.setSecurityRealm(github_realm)
    Jenkins.instance.save()
}


// Configuring security realm
import hudson.security.*
import com.cloudbees.plugins.credentials.*

def instance = Jenkins.getInstance()
def strategy = new hudson.security.ProjectMatrixAuthorizationStrategy()
String jenkins_admin_group = 'wuakitv*systems-operations'
String jenkins_wuaki_group = 'wuakitv'

// Roles based on https://wiki.jenkins-ci.org/display/JENKINS/Matrix-based+security
//Overall - http://javadoc.jenkins-ci.org/jenkins/model/Jenkins.html
strategy.add(Jenkins.ADMINISTER, jenkins_admin_group)
strategy.add(Jenkins.RUN_SCRIPTS, jenkins_admin_group)
strategy.add(Jenkins.READ, jenkins_admin_group)
// Agent (Slave < 2.0) - http://javadoc.jenkins-ci.org/jenkins/model/Jenkins.MasterComputer.html
strategy.add(Jenkins.MasterComputer.BUILD, jenkins_admin_group)
strategy.add(Jenkins.MasterComputer.CONFIGURE, jenkins_admin_group)
strategy.add(Jenkins.MasterComputer.CONNECT, jenkins_admin_group)
strategy.add(Jenkins.MasterComputer.CREATE, jenkins_admin_group)
strategy.add(Jenkins.MasterComputer.DELETE, jenkins_admin_group)
strategy.add(Jenkins.MasterComputer.DISCONNECT, jenkins_admin_group)
// Job - http://javadoc.jenkins-ci.org/hudson/model/Item.html
strategy.add(hudson.model.Item.BUILD, jenkins_admin_group)
strategy.add(hudson.model.Item.CANCEL, jenkins_admin_group)
strategy.add(hudson.model.Item.CONFIGURE, jenkins_admin_group)
strategy.add(hudson.model.Item.CREATE, jenkins_admin_group)
strategy.add(hudson.model.Item.DELETE, jenkins_admin_group)
strategy.add(hudson.model.Item.DISCOVER, jenkins_admin_group)
strategy.add(hudson.model.Item.EXTENDED_READ, jenkins_admin_group)
strategy.add(hudson.model.Item.READ, jenkins_admin_group)
strategy.add(hudson.model.Item.WIPEOUT, jenkins_admin_group)
strategy.add(hudson.model.Item.WORKSPACE, jenkins_admin_group)
// Run - http://javadoc.jenkins-ci.org/hudson/model/Run.html
strategy.add(hudson.model.Run.DELETE, jenkins_admin_group)
strategy.add(hudson.model.Run.UPDATE, jenkins_admin_group)
strategy.add(hudson.model.Run.ARTIFACTS, jenkins_admin_group)
// View - http://javadoc.jenkins-ci.org/hudson/model/View.html
strategy.add(hudson.model.View.CONFIGURE, jenkins_admin_group)
strategy.add(hudson.model.View.CREATE, jenkins_admin_group)
strategy.add(hudson.model.View.DELETE, jenkins_admin_group)
strategy.add(hudson.model.View.READ, jenkins_admin_group)
// SCM - http://javadoc.jenkins-ci.org/hudson/model/View.html
strategy.add(hudson.scm.SCM.TAG, jenkins_admin_group)
// Credentials - https://github.com/jenkinsci/credentials-plugin/blob/master/src/main/java/com/cloudbees/plugins/credentials/CredentialsProvider.java
strategy.add(CredentialsProvider.CREATE, jenkins_admin_group)
strategy.add(CredentialsProvider.UPDATE, jenkins_admin_group)
strategy.add(CredentialsProvider.VIEW, jenkins_admin_group)
strategy.add(CredentialsProvider.DELETE, jenkins_admin_group)
strategy.add(CredentialsProvider.MANAGE_DOMAINS, jenkins_admin_group)
// Plugin Manager http://javadoc.jenkins-ci.org/hudson/PluginManager.html
strategy.add(hudson.PluginManager.UPLOAD_PLUGINS, jenkins_admin_group)
strategy.add(hudson.PluginManager.CONFIGURE_UPDATECENTER, jenkins_admin_group)

strategy.add(hudson.model.Item.BUILD, jenkins_wuaki_group)
strategy.add(hudson.model.Item.READ, jenkins_wuaki_group)
strategy.add(Jenkins.READ, jenkins_wuaki_group)

instance.setAuthorizationStrategy(strategy)
instance.save()

// Configuring default security configuration
import hudson.security.csrf.DefaultCrumbIssuer

instance.setCrumbIssuer(new DefaultCrumbIssuer(true))

Set<String> agentProtocolsList = ['JNLP4-connect', 'Ping']
instance.setAgentProtocols(agentProtocolsList)

instance.getDescriptor("jenkins.CLI").get().setEnabled(false)

def rule = Jenkins.instance.getExtensionList(jenkins.security.s2m.MasterKillSwitchConfiguration.class)[0].rule
rule.setMasterKillSwitch(true)
Jenkins.instance.getExtensionList(jenkins.security.s2m.MasterKillSwitchWarning.class)[0].disable(true)
Jenkins.instance.save()

instance.save()
