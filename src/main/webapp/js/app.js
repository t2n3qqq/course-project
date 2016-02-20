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


function ArticleController($scope, $rootScope, $location, $cookieStore, CodeService) {

	$scope.articles = CodeService.query();
	$scope.article = new CodeService();


};

function ProfileController($scope, $rootScope, $location, $cookieStore, CodeService) {

};

function CreateController($scope, $rootScope, $location, $cookieStore, CodeService) {
	$scope.article = new CodeService();
	$scope.saveart = function() {
		debugger;
		delete $scope.article.id;
		var topics = new Array();
		topics.push($scope.article.topics);
		var tags = new Array();
		tags.push($scope.article.tags);
		//delete $scope.article.topics;
		$scope.article.topicslist = topics;
		$scope.article.tagslist = tags;
		$scope.article.$save(function() {
			$location.path('/');
			$scope.codes = CodeService.query();
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

function IndexController($scope, $rootScope, $location, CodeService) {
	$scope.articles = CodeService.query();
	$scope.article = new CodeService();

	$scope.showArt = function () {
		$location.path("/article");
	};

	//$scope.code.userId = $rootScope.user.name;
	//$scope.code.content = $scope.codeInput;
	$scope.save = function(codeTitle) {
		delete $scope.code.id;
		$scope.code.$save(function() {
			$location.path('/');
			$scope.codes = CodeService.query();
		});
	};
	$scope.deleteEntry = function(code) {
		code.$remove(function() {
			$scope.codes = CodeService.query();
		});
	};
	$scope.editEntry = function(code){
		$scope.code.content = code.content;
	};
	$scope.saveEntry = function(code) {
		$scope.code.userId = code.userId;
		$scope.code.title = code.title;
		$scope.code.id = code.id;
		$scope.code.$save(function() {
			$location.path('/');
			$scope.codes = CodeService.query();
		});
	};


	//+++++++++++++++++++

//	$scope.memory = new Array(100);
//	for(var i = 0; i < 100; i++){
//		$scope.memory[i] = 0;
//	}
//
//	Commands = {};
//	Commands["+"] = function(){$scope.memory[$scope.memoryPointer]++;}
//	Commands["-"] = function(){$scope.memory[$scope.memoryPointer]--;}
//	Commands[">"] = function(){
//		if($scope.memory.length <= $scope.memoryPointer+1){
//			$scope.memory.push(0);
//		};
//		$scope.memoryPointer++;
//	};
//	Commands["<"] = function(){$scope.memoryPointer--;}
//	Commands["["] = function(){$scope.loopstack.push($scope.stringPointer);}
//	Commands["]"] = function(){
//		if($scope.memory[$scope.memoryPointer] > 0){
//			$scope.loop()
//		}else {
//			$scope.loopstack.pop();
//		}
//	};
//	Commands["."] = function(){
//		var char = String.fromCharCode($scope.memory[$scope.memoryPointer]);
//		$scope.standardOutput.push(char);
//	}
//	Commands[','] = function() {
//		var input = "";
//		while (input.length == 0) {
//			input = prompt("enter one character for input");
//		}
//
//		var value = input.charCodeAt(0);
//		$scope.standardInput.push(input.charAt(0));
//		$scope.memory[$scope.memoryPointer] = value;
//	}
//	Commands["#"] = function(){
//		$scope.breakpoint = true;
//	}
//	$scope.breakpoint = false;
//	$scope.standardInput = [];
//	$scope.standardOutput = [];
//	$scope.loop = function(){
//		$scope.stringPointer = $scope.loopstack[$scope.loopstack.length - 1];
//	};
//	$scope.reset = function(){
//		for(var i = 0; i < 100; i++){
//			$scope.memory[i] = 0;
//		}
//		$scope.standardInput = [];
//		$scope.standardOutput = [];
//		$scope.memoryPointer = 0;
//		$scope.stringPointer = 0;
//		$scope.loopstack = [];
//	};
//	$scope.reset();
//	$scope.runapp = function(){
//		if($scope.code.content[$scope.stringPointer-1] == "#"){$scope.breakpoint = false;}
//		while (($scope.stringPointer < $scope.code.content.length)&&($scope.breakpoint == false)){
//			$scope.nextstep();
//		};
//	}
//	$scope.nextstep = function(){
//		if($scope.stringPointer < $scope.code.content.length){
//			Commands[$scope.code.content[$scope.stringPointer]].call();
//			$scope.stringPointer++;
//		};
//	};
//
};


//app.controller('brainfuckCtrl', function($scope){
//	Commands = {};
//	Commands["+"] = function(){$scope.memoryM[$scope.memoryPointer]++;}
//	Commands["-"] = function(){$scope.memoryM[$scope.memoryPointer]--;}
//	Commands[">"] = function(){
//		if($scope.memoryM.length <= $scope.memoryPointer+1){
//			$scope.memoryM.push(0);
//		};
//		$scope.memoryPointer++;
//	};
//	Commands["<"] = function(){$scope.memoryPointer--;}
//	Commands["["] = function(){$scope.loopstack.push($scope.stringPointer);}
//	Commands["]"] = function(){
//		if($scope.memoryM[$scope.memoryPointer] > 0){
//			$scope.loop()
//		}else {
//			$scope.loopstack.pop();
//		}
//	};
//	Commands["."] = function(){
//		var char = String.fromCharCode($scope.memoryM[$scope.memoryPointer]);
//		$scope.output += char + " ";
//	}
//	$scope.loop = function(){
//		$scope.stringPointer = $scope.loopstack[$scope.loopstack.length - 1];
//	};
//	$scope.reset = function(){
//		$scope.memoryM = new Array(100);
//		for(var i = 0; i < 100; i++){
//			$scope.memoryM[i] = 0;
//		}
//		//$scope.memoryM[0] = 0;
//		$scope.output = "";
//		$scope.memoryPointer = 0;
//		$scope.stringPointer = 0;
//		$scope.loopstack = [];
//	};
//	$scope.reset();
//	$scope.run = function(){
//
//		while ($scope.stringPointer < $scope.codeInput.length){
//			$scope.nextstep();
//		};
//	}
//	$scope.nextstep = function(){
//		if($scope.stringPointer < $scope.codeInput.length){
//			Commands[$scope.codeInput[$scope.stringPointer]].call();
//			$scope.stringPointer++;
//		};
//	};
//});

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

	return $resource('rest/news/:id', {id: '@articleId', title: '@name',content: '@content', topicslist: "@topicslist", tagslist: "@tagslist"});
});

