<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ include file="include.jsp" %>
<%--<!DOCTYPE html>--%>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js"> <!--<![endif]-->
	<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title>MultiGate</title>
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<meta name="description" content="" />
	<meta name="keywords" content="" />
	<meta name="author" content="" />
  	<!-- Facebook and Twitter integration -->
	<meta property="og:title" content=""/>
	<meta property="og:image" content=""/>
	<meta property="og:url" content=""/>
	<meta property="og:site_name" content=""/>
	<meta property="og:description" content=""/>
	<meta name="twitter:title" content="" />
	<meta name="twitter:image" content="" />
	<meta name="twitter:url" content="" />
	<meta name="twitter:card" content="" />

  	<!-- Place favicon.ico and apple-touch-icon.png in the root directory -->
  	<link rel="shortcut icon" href="favicon.ico">

  	<!-- Google Webfont -->
	<%--<link href='http://fonts.useso.com/css?family=Lato:300,400,700' rel='stylesheet' type='text/css'>--%>
	<!-- Themify Icons -->
	<link rel="stylesheet" href="index/css/themify-icons.css">
	<!-- Bootstrap -->
	<link rel="stylesheet" href="index/css/bootstrap.css">
	<!-- Owl Carousel -->
	<link rel="stylesheet" href="index/css/owl.carousel.min.css">
	<link rel="stylesheet" href="index/css/owl.theme.default.min.css">
	<!-- Magnific Popup -->
	<link rel="stylesheet" href="index/css/magnific-popup.css">
	<!-- Superfish -->
	<link rel="stylesheet" href="index/css/superfish.css">
	<!-- Easy Responsive Tabs -->
	<link rel="stylesheet" href="index/css/easy-responsive-tabs.css">
	<!-- Animate.css -->
	<link rel="stylesheet" href="index/css/animate.css">
	<!-- Theme Style -->
	<link rel="stylesheet" href="index/css/style.css">

	<!-- Modernizr JS -->
	<script src="index/js/modernizr-2.6.2.min.js"></script>
	<!-- FOR IE9 below -->
	<!--[if lt IE 9]>
	<script src="index/js/respond.min.js"></script>
	<![endif]-->


	</head>
	<body>

		<!-- START #fh5co-header -->
		<header id="fh5co-header-section" role="header" class="" >
			<div class="container">

				

				<!-- <div id="fh5co-menu-logo"> -->
					<!-- START #fh5co-logo -->
					<h1 id="fh5co-logo" class="pull-left"><a href="index.jsp"><img src="index/images/logo.png" alt="MultiGate Free HTML5 Template"></a></h1>
					
					<!-- START #fh5co-menu-wrap -->
					<nav id="fh5co-menu-wrap" role="navigation">
						
						
						<ul class="sf-menu" id="fh5co-primary-menu">
							<li class="active">
								<a href="">Home</a>
							</li>
							<li>
								<a href="#" class="fh5co-sub-ddown">Dropdown</a>
								 <ul class="fh5co-sub-menu">
								 	<li><a href="left-sidebar.html">Left Sidebar</a></li>
								 	<li><a href="right-sidebar.html">Right Sidebar</a></li>
									<li>
										<a href="#" class="fh5co-sub-ddown">Free HTML5</a>
										<ul class="fh5co-sub-menu">
											<li><a href="http://sc.chinaz.com/?item=build-free-html5-bootstrap-template" target="_blank">Build</a></li>
											<li><a href="http://sc.chinaz.com/?item=work-free-html5-template-bootstrap" target="_blank">Work</a></li>
											<li><a href="http://sc.chinaz.com/?item=light-free-html5-template-bootstrap" target="_blank">Light</a></li>
											<li><a href="http://sc.chinaz.com/?item=relic-free-html5-template-using-bootstrap" target="_blank">Relic</a></li>
											<li><a href="http://sc.chinaz.com/?item=display-free-html5-template-using-bootstrap" target="_blank">Display</a></li>
											<li><a href="http://sc.chinaz.com/?item=sprint-free-html5-template-bootstrap" target="_blank">Sprint</a></li>
										</ul>
									</li>
									<li><a href="#">CSS3</a></li> 
								</ul>
							</li>
							<li><a href="#">Elements</a></li>
							<li class="fh5co-special"><a href="#">
								Hi,
								<shiro:guest>Guest !</shiro:guest>
								<shiro:user>

										<%
											request.setAttribute("account",org.apache.shiro.SecurityUtils.getSubject().getPrincipals().oneByType(java.util.Map.class));
										%>
									<c:out value="${account.givenName}"/> !
								</shiro:user>

							</a></li>
						</ul>
					</nav>
				<!-- </div> -->

			</div>
		</header>
		
		
		<div id="fh5co-hero">
			<a href="#fh5co-main" class="smoothscroll fh5co-arrow to-animate hero-animate-4"><i class="ti-angle-down"></i></a>
			<!-- End fh5co-arrow -->
			<div class="container">
				<div class="col-md-8 col-md-offset-2">
					<div class="fh5co-hero-wrap">
						<div class="fh5co-hero-intro">
							<h1 class="to-animate hero-animate-1">MultiGate. Tools Center</h1>
							<h2 class="to-animate hero-animate-2">Rongcapital</h2>
							<p class="to-animate hero-animate-3">
								<shiro:guest>
									<a href="login.jsp" class="btn btn-outline btn-lg">LOG IN!</a>
								</shiro:guest>
								<shiro:user>
									<a href="#" class="btn btn-outline btn-lg">WELCOME</a>
								</shiro:user>
							</p>
						</div>
					</div>
				</div>
			</div>		
		</div>

		<div id="fh5co-main">
	
			<div class="container">
				<div class="row" id="fh5co-features">
					
					<div class="col-md-4 col-sm-6 text-center fh5co-feature feature-box" onclick="window.location.href='trade/trade.html'">
						<div class="fh5co-feature-icon">
							<i class="ti-mobile"></i>
						</div>
						<h3 class="heading">Trade</h3>
						<p>Trade page for TongLian AgentPay.Single and Batch mode are both supported here.</p>
					</div>
					<div class="col-md-4 col-sm-6 text-center fh5co-feature feature-box" onclick="window.location.href='redis/redisUtil.html'">
						<div class="fh5co-feature-icon">
							<i class="ti-lock"></i>
						</div>
						<h3 class="heading">Redis</h3>
						<p>Little Redis utils makes our common daily work easily.But only for key-value operation.</p>
					</div>

					<div class="clearfix visible-sm-block"></div>

					<div class="col-md-4 col-sm-6 text-center fh5co-feature feature-box" onclick="window.location.href='auth/auth.html'">
						<div class="fh5co-feature-icon">
							<i class="ti-video-camera"></i>
						</div>
						<h3 class="heading">Auth</h3>
						<p>Auth page for TongLian interface .No more hard code to verify a card.</p>
					</div>

					<div class="clearfix visible-md-block visible-lg-block"></div>

					<div class="col-md-4 col-sm-6 text-center fh5co-feature feature-box" onclick="window.location.href='qrcode.html'">
						<div class="fh5co-feature-icon">
							<i class="ti-shopping-cart"></i>
						</div>
						<h3 class="heading">QRcode</h3>
						<p>Input some message you need ,create a QRcode picture in HTML5 canvas. </p>
					</div>

					<div class="clearfix visible-sm-block"></div>

					<div class="col-md-4 col-sm-6 text-center fh5co-feature feature-box" onclick="window.location.href='release/release.html'">
						<div class="fh5co-feature-icon">
							<i class="ti-palette"></i>
						</div>
						<h3 class="heading">Release</h3>
						<p>Release page for LianLian.Cancel a contract for an account. </p>
					</div>

				</div>
				<!-- END row -->

				<div class="fh5co-spacer fh5co-spacer-md"></div>
				<!-- End Spacer -->

				<div class="row" id="fh5co-works">
					<div class="col-md-8 col-md-offset-2 text-center fh5co-section-heading work-box">
						<h2 class="fh5co-lead">Awesome Projects</h2>
						<p class="fh5co-sub">Every fascinating tech we learned ,we programmed and uploaded in github can be found in here.It will recognize the way we growing up and the effort we payed for life.</p>
						<div class="fh5co-spacer fh5co-spacer-sm"></div>
					</div>
					<div class="col-md-4 col-sm-6 col-xs-6 col-xxs-12 text-center fh5co-work-item work-box">
						<figure><a href="https://github.com/SWGawain/MultiGate" _blank><img class="img-responsive" src="index/images/work_1.jpg" alt="Free HTML5 Template"></a></figure>
						<p class="fh5co-category">This is the current project.</p>
						<h3 class="heading">MultiGates</h3>
					</div>
					<div class="col-md-4 col-sm-6 col-xs-6 col-xxs-12 text-center fh5co-work-item work-box"> 
						<figure><a href="https://github.com/SWGawain/ShiroLearning/" _blank><img class="img-responsive" src="index/images/work_2.jpg" alt="Free HTML5 Template"></a></figure>
						<p class="fh5co-category">Shiro demo with Spring and Stormpath.</p>
						<h3 class="heading">Shiro-spring-stormpath</h3>
					</div>

					<div class="col-md-4 col-md-offset-4 text-center work-box">
						<p><a href="https://github.com/SWGawain" class="btn btn-outline btn-md" _blank>Visit Github</a></p>
					</div>
				</div>
				<!-- END row -->
				
				<div class="fh5co-spacer fh5co-spacer-md"></div>
				<div class="row">
					<!-- Start Slider Testimonial -->
	            <h2 class="fh5co-uppercase-heading-sm text-center animate-box">Famous speech...</h2>
	            <div class="fh5co-spacer fh5co-spacer-xs"></div>
	            <div class="owl-carousel-fullwidth animate-box">
	            <div class="item">
	              <p class="text-center quote">&ldquo;As long as it is women ,nothing would be a problem . &rdquo; <cite class="author">&mdash;Dubbo Wu</cite></p>
	            </div>
	            <div class="item">
	              <p class="text-center quote">&ldquo;Creativity is just connecting things. When you ask creative people how they did something, they feel a little guilty because they didn’t really do it, they just saw something. It seemed obvious to them after a while. &rdquo;<cite class="author">&mdash; Steve Jobs</cite></p>
	            </div>
	            <div class="item">
	              <p class="text-center quote">&ldquo;I think design would be better if designers were much more skeptical about its applications. If you believe in the potency of your craft, where you choose to dole it out is not something to take lightly. &rdquo;<cite class="author">&mdash; Frank Chimero</cite></p>
	            </div>
	          </div>
	           <!-- End Slider Testimonial -->
				</div>
				<!-- END row -->
				<div class="fh5co-spacer fh5co-spacer-md"></div>

			</div>
			<!-- END container -->

		
		</div>
		<!-- END fhtco-main -->


		<footer role="contentinfo" id="fh5co-footer">
			<a href="#" class="fh5co-arrow fh5co-gotop footer-box"><i class="ti-angle-up"></i></a>
			<div class="container">
				<div class="row">
					<div class="col-md-4 col-sm-6 footer-box">
						<h3 class="fh5co-footer-heading">About us</h3>
						<p>Zhaowei Mansion C2-2     .Beijing ChaoYang</p>
						<p><a href="#" class="btn btn-outline btn-sm">Donate us</a></p>

					</div>
					<div class="col-md-4 col-sm-6 footer-box">
						<h3 class="fh5co-footer-heading">Links</h3>
						<ul class="fh5co-footer-links">
							<li><a href="#">Terms &amp; Conditions</a></li>
							<li><a href="#">Our Careers</a></li>
							<li><a href="#">Support &amp; FAQ's</a></li>
							<li><a href="#">Sign up</a></li>
							<li><a href="#">Log in</a></li>
						</ul>
					</div>
					<div class="col-md-4 col-sm-12 footer-box">
						<h3 class="fh5co-footer-heading">Get in touch</h3>
						<ul class="fh5co-social-icons">
							
							<li><a href="#"><i class="ti-google"></i></a></li>
							<li><a href="#"><i class="ti-twitter-alt"></i></a></li>
							<li><a href="#"><i class="ti-facebook"></i></a></li>	
							<li><a href="#"><i class="ti-instagram"></i></a></li>
							<li><a href="#"><i class="ti-dribbble"></i></a></li>
						</ul>
					</div>
					<div class="col-md-12 footer-box">
						<div class="fh5co-copyright">
						<p>Copyright &copy; 2016. MultiGate Entertainment  All rights reserved.</p>
						</div>
					</div>
					
				</div>
				<!-- END row -->
				<div class="fh5co-spacer fh5co-spacer-md"></div>
			</div>
		</footer>
			
			
		<!-- jQuery -->
		<script src="index/js/jquery-1.10.2.min.js"></script>
		<!-- jQuery Easing -->
		<script src="index/js/jquery.easing.1.3.js"></script>
		<!-- Bootstrap -->
		<script src="index/js/bootstrap.js"></script>
		<!-- Owl carousel -->
		<script src="index/js/owl.carousel.min.js"></script>
		<!-- Magnific Popup -->
		<script src="index/js/jquery.magnific-popup.min.js"></script>
		<!-- Superfish -->
		<script src="index/js/hoverIntent.js"></script>
		<script src="index/js/superfish.js"></script>
		<!-- Easy Responsive Tabs -->
		<script src="index/js/easyResponsiveTabs.js"></script>
		<!-- FastClick for Mobile/Tablets -->
		<script src="index/js/fastclick.js"></script>
		<!-- Parallax -->
		<%--<script src="index/js/jquery.parallax-scroll.min.js"></script>--%>
		<!-- Waypoints -->
		<script src="index/js/jquery.waypoints.min.js"></script>
		<!-- Main JS -->
		<script src="index/js/main.js"></script>

		<!-- shiro JS -->
		<%--<script src="index/js/shiro.js"></script>--%>

	</body>
</html>
