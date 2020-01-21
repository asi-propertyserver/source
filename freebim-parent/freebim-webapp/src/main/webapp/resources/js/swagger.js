window.onload = () => {
    const ui = SwaggerUIBundle({
        url: "/v2/api-docs",
        dom_id: '#swagger-container',
        deepLinking: true,
      presets: [
        SwaggerUIBundle.presets.apis,
        //SwaggerUIStandalonePreset
      ],
      plugins: [
        SwaggerUIBundle.plugins.DownloadUrl
      ],
      //layout: "StandaloneLayout" 
      })

      window.ui = ui;
};
