angular.module('ionicApp', ['ionic'])

.config(function($stateProvider, $urlRouterProvider,$ionicConfigProvider) {
  // 解决安卓机导航在顶部bug
  $ionicConfigProvider.platform.ios.tabs.style('standard'); 
  $ionicConfigProvider.platform.ios.tabs.position('bottom');
  $ionicConfigProvider.platform.android.tabs.style('standard');
  $ionicConfigProvider.platform.android.tabs.position('bottom');

  $ionicConfigProvider.platform.ios.navBar.alignTitle('center'); 
  $ionicConfigProvider.platform.android.navBar.alignTitle('center');

  $ionicConfigProvider.platform.ios.backButton.previousTitleText('').icon('ion-ios-arrow-thin-left');
  $ionicConfigProvider.platform.android.backButton.previousTitleText('').icon('ion-android-arrow-back');        

  $ionicConfigProvider.platform.ios.views.transition('ios'); 
  $ionicConfigProvider.platform.android.views.transition('android');
  $ionicConfigProvider.views.maxCache(0);

  $stateProvider
  .state('tabs', {
    url: "/tab",
    abstract: true,
    templateUrl: "/html/tabs.html"
  })
  .state('tabs.dialogue', {
    url: "/dialogue",
    views: {
      'dialogue-tab': {
        templateUrl: "/html/dialogue.html",
        controller: 'dialogueCtrl'
      }
    }
  })       
  .state('tabs.collect', {
    url: "/collect",
    views: {
      'collect-tab': {
        templateUrl: "/html/collect.html",
        controller: 'collectTabCtrl'
      }
    }
  })
  .state('tabs.navstack', {
    url: "/navstack",
    views: {
      'collect-tab': {
        templateUrl: "/html/headEdit.html",
        controller: 'headEditTabCtrl'
      }
    }
  })
  .state('tabs.mine', {
    url: "/mine",
    views: {
      'mine-tab': {
        templateUrl: "/html/mine.html",
        controller: 'mineCtrl'
      }
    }
  });


  $urlRouterProvider.otherwise("/tab/dialogue");

})
    .controller('mineCtrl', function($scope,$state) {
        $scope.Logout = function(){
            window.location.href = "/";
        }
        $(".single-member").click(function(){
            if(!$(".member-image").hasClass("member-info_focus")){
                $(".member-image").addClass("member-info_focus");
            }else{
                $(".member-image").removeClass("member-info_focus");
            }
        });
        // $(".container").click(function(){
        //     if($(".member-image").hasClass("member-info_focus")){
        //         $(".member-image").removeClass("member-info_focus");
        //     }
        // })
        $scope.user = {};
        $scope.user.motto = "这个人很懒,什么都没有留下.";
        $scope.user.motto_eng = "The peson is lazy,leaving nothing.";
        $.ajax({
            type: "get",
            url: "/query_user_main_page",
            success: function (data) {
               console.log(data);
                $scope.user.name = data.user_name;
                $scope.user.email = data.email;
                $scope.user.phone = data.phone_number;
                if (data.motto) {
                    $scope.user.motto = data.motto;
                }
            },
            error: function () {
                console.log("login error");
            }
        })

    })
.controller('collectTabCtrl', function($scope,$state) {
    function fclick(obj){
        obj.style.posTop=event.srcElement.offsetTop
        var x=event.x-obj.offsetWidth/2
        if(x<event.srcElement.offsetLeft)x=event.srcElement.offsetLeft
        if(x>event.srcElement.offsetLeft+event.srcElement.offsetWidth-obj.offsetWidth)x=event.srcElement.offsetLeft+event.srcElement.offsetWidth-obj.offsetWidth
        obj.style.posLeft=x
    }
  // console.log("你好");
  $scope.createCharacter = function(){
    $state.go("tabs.navstack");
  }
})
.controller('headEditTabCtrl', function($scope,$state,$ionicPopup, $timeout,appService,$rootScope) {
    console.log("进入初始化人物形象");
    //初始化人物形象
    $rootScope.robot = {};
    $scope.robot = {};
    $scope.initRobot={};
    // step1：从后端加载上一次用户存储的人物数据放到person中
    appService.queryVirtualCharacter().then(function (data) {
        console.log(data);
        $rootScope.robot.robotName = data.robot_name;
        if (data == "") {
            $scope.initRobot = personInit;
        } else {
            $scope.initRobot = angular.fromJson(data.robot_setting);
        }
        // step2：初始化人物形象
        $.getScript("../js/cute_gravatar_master/main.js", function () {
            initHeadEdit($scope.initRobot);
        });
    });

    $scope.saveHead = function () {
        // 点击save之后将changePerson传到后端
        // 读取后端人物数据时，将其变成所需对象，然后调用recordTag(对象);然后调用初始化函数personInitA()即可
        //console.log(person);
        $scope.showPopup();
       }
    $scope.saveRobot = function(name){
        // console.log("改变后的人物:"+angular.toJson(changePerson));

        $scope.robot.setting = angular.toJson(changePerson);
        $scope.robot.name = name;
        // console.log($scope.robot.setting);
        // appService.addCharacter($scope.robot.name,$scope.robot.setting).then(function (data) {
        //     console.log(data);
        // });
        $.ajax({
            type: "post",
            url: "/add_robot",
            data: "robot_name="+ $scope.robot.name+"&robot_setting="+$scope.robot.setting,

            success: function (data) {
                if(data=="Ok"){
                    //保存名字到全局变量
                    $rootScope.robotName = data.robot_name;
                    //跳转
                    $state.go("tabs.collect");
                }
            },
            error: function () {
                console.log("login error");
            }
        })

    }
    $scope.showPopup = function () {
        $scope.data = {}
            var myPopup = $ionicPopup.show({
                template: '<input type="text" ng-model="data.name">',
                title: 'Ta的名字？',
                scope: $scope,
                buttons: [
                    { text: 'Cancel' },
                    {
                        text: '<b>Save</b>',
                        type: 'button-positive',
                        onTap: function(e) {
                            if (!$scope.data.name) {
                                // 不允许用户关闭，除非输入 wifi 密码
                                e.preventDefault();
                            } else {
                                $scope.saveRobot($scope.data.name);
                            }
                        }
                    },
                ]
            });

    };

})
.controller('dialogueCtrl',function($scope,$state,appService){
  //判断是否用户已经设定好虚拟人物了。显示不同的界面
    $scope.flag = false;
    $scope.initRobot = {};
  appService.queryVirtualCharacter().then(function(data) {
      if(data==""){
          $scope.flag = false;
          $scope.initRobot = personInit;
      }else{
          $scope.flag = true;
          $scope.initRobot = angular.fromJson(data.robot_setting);
      }
      // step2：初始化人物形象
      $.getScript("../js/cute_gravatar_master/main.js",function(){
          initHeadEdit($scope.initRobot);
          d3.select("#contentSVG").attr("height",$(window).get(0).innerHeight);
          // 聊天背景
          $("#background2").hide();
          $("#background3").hide();
          $("#background").hide();
          $("svg").css("background","none");
          $("#contentDIV").css("background","url(../images/dialogue-background2.jpg) no-repeat -15rem 0rem");
      });

  });
      // 加载虚拟人物
    if($scope.flag){
        $.getScript("/js/cute_gravatar_master/main.js",function(){
            initHeadEdit(changePerson);
            d3.select("#contentSVG").attr("height",$(window).get(0).innerHeight);
            // 聊天背景
            $("#background2").hide();
            $("#background3").hide();
            $("#background").hide();
            $("svg").css("background","none");
            $("#contentDIV").css("background","url(../images/dialogue-background2.jpg) no-repeat -15rem 0rem");
        });
        $.getScript("/js/dialogue_texiao.js");
    }else{
        // $.getScript("/js/font_rain.js");
        $.getScript("/particles/particles.min.js");
        $.getScript("/particles/js/app.js");
        $.getScript("/particles/js/lib/stats.js",function () {
            var count_particles, stats, update;
              stats = new Stats;
            stats.setMode(0);
            stats.domElement.style.position = 'absolute';
            stats.domElement.style.left = '0px';
            stats.domElement.style.top = '0px';
            document.body.appendChild(stats.domElement);
            count_particles = document.querySelector('.js-count-particles');
            update = function() {
                stats.begin();
                stats.end();
                // if (window.pJSDom[0].pJS.particles && window.pJSDom[0].pJS.particles.array) {
                //     // count_particles.innerText = window.pJSDom[0].pJS.particles.array.length;
                // }
                requestAnimationFrame(update);
            };
            requestAnimationFrame(update);
        });
        $youziku.load("body", "ba74badaa6dd497fa72f409e5fb3e874", "hdjxyt");
        $youziku.load("#peiban1,#peiban2", "ba74badaa6dd497fa72f409e5fb3e874", "hdjxyt");

        $youziku.draw();
    }
    $scope.person = {question:""};
   var message =  $scope.person.question;
    $scope.person.answer = "快来跟我聊天吧";
  $scope.submitQuestion = function(){
       // message= "你好";
      message =  $scope.person.question;
      $.ajax({
          type: "post",
          url: "/handle_message",
          data:"message="+message,
          dataType: "text",
          success: function (data) {
              $("#personAnswer").text(data);
              $scope.person.answer =data;
              $("#question").val("");
          },
          error: function () {
              console.log("received message failed")
          }
      })
  //     console.log("请求中的$scope.question:"+$scope.person.question);
  //   appService.dialogueSubmit($scope.person.question).then(function(data){
  //     console.log(data);
  //   //显示后端传来的答案
  //       $scope.person.answer = data;
  // });
  }


})


.service('appService',['$http','$window','$q',function ($http,$window,$q) {
 this.login = function (username,password) {
  var deferred = $q.defer();
  $http({
    url: "/login_verification",
    method: 'post',
    cache: false,
    data: {user_name: username, user_password: password},
    // headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
  }).then(function (results) {
    deferred.resolve(results.data);
  }).catch(function (e) {
  });
  return deferred.promise;
};
this.dialogueSubmit = function (dialogue) {
    console.log("service中dialogue:"+dialogue);
  var deferred = $q.defer();
  $http({
    url: "/handle_message",
    method: 'post',
    cache: false,
    data: "message="+dialogue,
      headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
  }).then(function (results) {
    deferred.resolve(results.data);
  }).catch(function (e) {
  });
  return deferred.promise;
};
// $scope.user.name 

this.queryVirtualCharacter = function () {
  var deferred = $q.defer();
  $http({
    url: "/query_robot",
    method: 'get',
    cache: false
  }).then(function (results) {
    // console.log(results);
    deferred.resolve(results.data);
  }).catch(function (e) {
  });
  return deferred.promise;
};
    // this.addCharacter = function (name,setting) {
    //     console.log("robot.name:"+name);
    //     var deferred = $q.defer();
    //     $http({
    //         url: "/add_robot",
    //         method: 'post',
    //         data: "robot_name="+name+"&robot_setting="+setting,
    //         // headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
    //         headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'},
    //         responseType: "text"
    //     })
        // .then(function (results) {
        //     console.log(results);
        //     deferred.resolve(results.data);
        // }).catch(function (e) {
        //     console.log("错了");
        // });
    //     return deferred.promise;
    // };
}]);