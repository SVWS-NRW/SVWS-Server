export const dataServerConnection = {
	server : {
		benutzername : "Admin",
		passwort: "",
		servername: (process.env.PLAYWRIGHT_svws_testing_api_host?? 'https://localhost') + (process.env.PLAYWRIGHT_svws_testing_api_port == null ? '' : (':' + process.env.PLAYWRIGHT_svws_testing_api_port)),
		serverurl:  (process.env.PLAYWRIGHT_svws_testing_api_host?? 'https://localhost') + (process.env.PLAYWRIGHT_svws_testing_api_port == null ? '' : (':' + process.env.PLAYWRIGHT_svws_testing_api_port)) + "/#/"
	}
}

