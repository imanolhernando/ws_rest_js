<!DOCTYPE html>
<html lang="es">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>WSRest</title>

    <!-- Bootstrap -->
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>
  <body>

	<main class="container">
		<div class="jumbotron">
		  <h1 class="display-4">Web Service API Rest </h1>
		  <p class="lead">Servicio Rest con Spring 4.3.0.RELEASE</p>
		  <p><b>Endpoint</b> http://localhost:8080/wsrest/api<p>
		   <p> <a href="<%=request.getContextPath() + "/swagger-ui.html#/" %>">API documentacion</a><p>		  
		</div>
	</main>	
		
	</body>
</html>			