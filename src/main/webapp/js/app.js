var app = angular.module('exampleApp', ['ngRoute', 'ngCookies', 'exampleApp.services'])
	.config(
		[ '$routeProvider', '$locationProvider', '$httpProvider', function($routeProvider, $locationProvider, $httpProvider) {

			$routeProvider.when('/login', {
				templateUrl: 'partials/login.html',
				controller: LoginController
			});

			$routeProvider.when('/article', {
				templateUrl: 'partials/article.html',
				controller: ArticleController
			});

			$routeProvider.when('/myarticles', {
				templateUrl: 'partials/myarticles.html',
				controller: MyarticlesController
			});

			$routeProvider.when('/profile', {
				templateUrl: 'partials/profile.html',
				controller: ProfileController
			});

			$routeProvider.when('/create', {
				templateUrl: 'partials/create.html',
				controller: CreateController
			});
			
			$routeProvider.otherwise({
				templateUrl: 'partials/index.html',
				controller: IndexController
			});
			
			$locationProvider.hashPrefix('!');
			
			/* Register error provider that shows message on failed requests or redirects to login page on
			 * unauthenticated requests */
		    $httpProvider.interceptors.push(function ($q, $rootScope, $location) {
			        return {
			        	'responseError': function(rejection) {
			        		var status = rejection.status;
			        		var config = rejection.config;
			        		var method = config.method;
			        		var url = config.url;
			      
			        		if (status == 401) {
			        			$location.path( "/login" );
			        		} else {
			        			$rootScope.error = method + " on " + url + " failed with status " + status;
			        		}
			              
			        		return $q.reject(rejection);
			        	}
			        };
			    }
		    );
		    
		    /* Registers auth token interceptor, auth token is either passed by header or by query parameter
		     * as soon as there is an authenticated user */
		    $httpProvider.interceptors.push(function ($q, $rootScope, $location) {
		        return {
		        	'request': function(config) {
		        		var isRestCall = config.url.indexOf('rest') == 0;
		        		if (isRestCall && angular.isDefined($rootScope.authToken)) {
		        			var authToken = $rootScope.authToken;
		        			if (exampleAppConfig.useAuthTokenHeader) {
		        				config.headers['X-Auth-Token'] = authToken;
		        			} else {
		        				config.url = config.url + "?token=" + authToken;
		        			}
		        		}
		        		return config || $q.when(config);
		        	}
		        };
		    }
	    );
		   
		} ]
		
	).run(function($rootScope, $location, $cookieStore, UserService) {
		
		/* Reset error when a new view is loaded */
		$rootScope.$on('$viewContentLoaded', function() {
			delete $rootScope.error;
		});
		
		$rootScope.hasRole = function(role) {
			
			if ($rootScope.user === undefined) {
				return false;
			}
			
			if ($rootScope.user.roles[role] === undefined) {
				return false;
			}
			
			return $rootScope.user.roles[role];
		};
		
		$rootScope.logout = function() {
			delete $rootScope.user;
			delete $rootScope.authToken;
			delete $rootScope.fcanvas;
			delete $rootScope.client;
			$cookieStore.remove('authToken');
			$location.path("/login");
		};
		$rootScope.editprofile = function() {
			$location.path("/profile");
		};
		$rootScope.createart = function() {
			$location.path("/create");
		};
		
		 /* Try getting valid user from cookie or go to login page */
		var originalPath = $location.path();
		$location.path("/login");
		var authToken = $cookieStore.get('authToken');
		if (authToken !== undefined) {
			$rootScope.authToken = authToken;
			UserService.get(function(user) {
				$rootScope.user = user;
				$location.path(originalPath);
			});
		}
		
		$rootScope.initialized = true;
	});


function ArticleController($scope, $rootScope, $location, $cookieStore, CommentService) {


	//$scope.articles = CodeService.query();
	$scope.article = $rootScope.showthisarticle;
	var elem = document.createElement("img");
	elem.src = $scope.article.imgurl;
	document.getElementById("imgsrc").appendChild(elem);

	$scope.isCurrentUser = $rootScope.user.name == $scope.article.thisuser || $rootScope.user.roles['admin'];
	$scope.editarticle = function(){
		$rootScope.editthisarticle = $scope.article;
		$rootScope.isCreateNew = false;
		$location.path("/create");
	};
	//$scope.deletearticle = function(){
	//	//$rootScope.editthisarticle = $scope.article;
	//	//$rootScope.isCreateNew = false;
	//	$scope.article.$delete();
	//	$location.path("/");
	//};
	$scope.comment = new CommentService();
	$scope.comment.articleId = $scope.article.articleId;
	$scope.comments = CommentService.getcomments($scope.comment);



	$scope.savecomment = function(){


		$scope.comment.username = $rootScope.user.name;
		$scope.comment.articleId = $scope.article.articleId;
		//$scope.comment.date = null;$.param({articleId: $scope.article.articleId, content: $scope.comment.content})
		CommentService.getMethod($scope.comment,function() {
			$scope.comments = CommentService.getcomments($scope.comment);
			//$location.path('/');
			//$scope.comments = CommentService.getcomments({articleId:$scope.article.articleId});
		});
	};



};

function ProfileController($scope, $rootScope, $location, $cookieStore, CodeService) {

};

function MyarticlesController($scope, $rootScope, $location, ArtService) {
	$scope.articles = ArtService.query();
	$scope.article = new ArtService();

};
function CreateController($scope, $rootScope, $location, $cookieStore, CodeService, TopicService) {

	//$scope.article = $rootScope.showthisarticle;
	$scope.topics = TopicService.query();

	if($rootScope.isCreateNew == false){
		$scope.article = $rootScope.editthisarticle;
		$rootScope.isCreateNew = true;
	}
	else{
		$scope.article = new CodeService();
	}




	$scope.loadImage = function(){
		imageLoader = document.getElementById('imageLoader');
		imageLoader.addEventListener('change', function(e) {
			var reader;
			reader = new FileReader;
			reader.onload = function(event) {
				var img;
				img = new Image;
				img.src = event.target.result;
				$scope.imgSrc = img.src;
				var elem = document.createElement("img");
				elem.src = $scope.imgSrc;
				document.getElementById("imgDiv").appendChild(elem);
			};

			reader.readAsDataURL(e.target.files[0]);

		}, false);


	};

	$scope.saveart = function() {
		$scope.selectedtopics = [];
		checkboxes = document.getElementById("topicsgroup").getElementsByTagName("input");
		for(var i = 0; i < checkboxes.length; i++){
			if(checkboxes[i].checked == true){
				$scope.selectedtopics.push(i);
			}
		};
		delete $scope.article.id;
		var topics = new Array();
		for(var i = 0; i < $scope.selectedtopics.length; i++){
			topics.push($scope.topics[$scope.selectedtopics[i]].name);
		};
		//topics.push($scope.article.topics);
		var tags = new Array();
		tags.push($scope.article.tags);
		//delete $scope.article.topics;
		$scope.article.topicslist = topics;
		$scope.article.tagslist = tags;
		//$scope.article.imgurl = $scope.imgurl;
		$scope.article.$save(function() {
			$location.path('/');
			$scope.articles = CodeService.query();
		});
	};

};


function LoginController($scope, $rootScope, $location, $cookieStore, UserService) {

	$scope.rememberMe = false;

	$scope.login = function() {
		UserService.authenticate($.param({username: $scope.username, password: $scope.password}), function(authenticationResult) {
			var authToken = authenticationResult.token;
			$rootScope.authToken = authToken;
			if ($scope.rememberMe) {
				$cookieStore.put('authToken', authToken);
			}
			UserService.get(function(user) {
				$rootScope.user = user;
				$location.path("/");
			});
		});
	};
	$scope.signup = function() {
		UserService.register($.param({username: $scope.username, password: $scope.password}), function(authenticationResult) {
			var authToken = authenticationResult.token;
			$rootScope.authToken = authToken;
			if ($scope.rememberMe) {
				$cookieStore.put('authToken', authToken);
			}
			UserService.get(function(user) {
				$rootScope.user = user;
				$location.path("/");
			});
		});
	};
};

function IndexController($scope, $rootScope, $location, CodeService, ArtService) {
	$scope.showmyarts = function(){
		$scope.articles = ArtService.query();
		//$location.path("/myarticles");
		//$scope.articles = CodeService.query();
		//$scope.article = new CodeService();
		//
		//titleT = "testpost title";
		//contentT = "testpost content";
		//CodeService.getuserarticles($.param({title: titleT, content: contentT}), function() {
        //
        //
		//	//$scope.articles = CodeService.query();
		//	//CodeService.get(function(str) {
		//	//	$rootScope.str = str;
		//	//	$location.path("/");
		//	//});
		//});
	}
	$scope.articles = CodeService.query();
	//$scope.articles = temparticles;

	$scope.article = new CodeService();

	$scope.press = function () {
		for (index = 0; index < $scope.articles.length; ++index) {
			console.log($scope.articles[index]);
		//	alert($scope.articles[index].tagslist + " " + $scope.articles[index].topicslist);
		}

	}


	$scope.showArt = function (article) {
		$rootScope.showthisarticle = article;
		$location.path("/article");

	};


};

var services = angular.module('exampleApp.services', ['ngResource']);

services.factory('UserService', function($resource) {
	
	return $resource('rest/user/:action', {},
			{
				authenticate: {
					method: 'POST',
					params: {'action' : 'authenticate'},
					headers : {'Content-Type': 'application/x-www-form-urlencoded'}
				},
				register: {
					method: 'POST',
					params: {'action' : 'register'},
					headers : {'Content-Type': 'application/x-www-form-urlencoded'}
				}
			}
		);
});

services.factory('CodeService', function($resource) {

	return $resource('rest/news/:id', {
				id: '@articleId',
				title: '@name',
				content: '@content',
				topicslist: "@topicslist",
				tagslist: "@tagslist",
				thisuser: "@thisuser",
				date: "@date",
				vote: "@vote",
				imgurl: "@imgurl"});
});
services.factory('ArtService', function($resource) {

	return $resource('rest/news/getuserarticles',
		{
			id: '@articleId',
			title: '@name',
			content: '@content',
			topicslist: "@topicslist",
			tagslist: "@tagslist",
			thisuser: "@thisuser",
			date: "@date",
			vote: "@vote",
			imgurl: "@imgurl"});
});

services.factory('CommentService', function($resource) {

	return $resource('rest/comments/:action', {
		id: '@commentId',
		username: "@username",
		articleId: "@articleId",
		content: "@content"
	}, {
			getcomments:{
				params: {'action' : 'getcomments'},
				method : 'POST' ,
				isArray:true
			},
		   	getMethod:{
				params: {'action' : 'getMethod'},
				method:'POST'
			//isArray:true
		}

	}
		);
});

services.factory('TopicService', function($resource) {

	return $resource('rest/topics/', {
			id: "@id",
			name: "@name"});
});

//services.factory('CodeService', function($resource) {
//
//	return $resource('rest/news/:action',{}, {
//			getallarticles:{id: '@articleId', title: '@name',content: '@content', topicslist: "@topicslist", tagslist: "@tagslist"},
//			getuserarticles:{method:'POST',params: {'action' : 'getuserarticles'}}
//
//	});
//});

