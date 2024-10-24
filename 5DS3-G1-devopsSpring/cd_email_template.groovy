def sendEmailNotification(currentBuild, env) {
    def subject = "Jenkins CD Pipeline Notification: ${currentBuild.fullDisplayName} - ${currentBuild.result}"
    def body = """
        <h2>Jenkins CD Pipeline Notification</h2>
        <p><strong>Project:</strong> ${env.SPRING_BOOT_PROJECT_NAME}</p>
        <p><strong>Build Number:</strong> ${currentBuild.number}</p>
        <p><strong>Build Status:</strong> ${currentBuild.result}</p>
        <p><strong>Docker Build Status:</strong> ${env.DOCKER_BUILD_STATUS}</p>
        <p><strong>Docker Build Duration:</strong> ${env.DOCKER_BUILD_DURATION}</p>
        <p><strong>Docker Push Status:</strong> ${env.DOCKER_PUSH_STATUS}</p>
        <p><strong>Docker Push Duration:</strong> ${env.DOCKER_PUSH_DURATION}</p>
        <p>For more details, check the <a href="${env.BUILD_URL}">Jenkins Job Page</a>.</p>
    """

    // Send email
    emailext(
        subject: subject,
        body: body,
        mimeType: 'text/html',
        to: env.EMAIL_TO,
        from: env.EMAIL_FROM
    )
}
