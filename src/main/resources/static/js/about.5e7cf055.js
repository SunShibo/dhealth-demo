(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["about"],{2502:function(t,s,i){"use strict";var e=i("5075"),a=i.n(e);a.a},2952:function(t,s){t.exports="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAABQ0lEQVQ4T63Ur0sFQRAH8O/ckxdMFpPJoPF+vJUXRPCCyWJSsGgRwWKxWESDxWKxWAQxyXv+KTt3F9TkHyG8IsLeyoDhWO7HnrhpYXc/OwMzQ/jnRXWeUuoVwFdZlgd5nr/3+bMJnADYBfBWluVeH7QWTNN0bjabvQDY6YvWgpKiUmoewBTAdh+0ERQ0juOFIAimRLTli7aCgiZJskhEgm76oJ2goFEULQ0GgwkRrXehXqCgYRguD4fDibV2rQ31BgUdj8erxhgpqUhQY8xGURSf1TrtBcrD0Wh0SURXsrfWrmRZ9vFnsIoR0b7W+tntIu8IHexQa/1U15JeoIMdaa0fmvq7E6xiAE6Y+b5tWLSCDnbKzHddk6cRdLAzZr7twuS8FnSwc2a+8cEaQaWU/QUumPnaF2uL8BjAd5Zlj30wufsDDjiPFSaZ3nQAAAAASUVORK5CYII="},"4c76":function(t,s,i){t.exports=i.p+"static/img/no_data.6c749b91.png"},5075:function(t,s,i){},"9cd8":function(t,s,i){"use strict";i.r(s);var e=function(){var t=this,s=t.$createElement,e=t._self._c||s;return e("div",[e("v-head",{attrs:{back:"webview",title:"体态评估"}}),t.initResult.length?e("div",{staticClass:"page"},t._l(t.initResult,(function(s,a){return e("div",{key:a,class:{list:!0,"list-t":"yes"===s.status,"list-f":"no"===s.status,"last-list":a===t.initResult.length-1},on:{click:function(i){return t.itemClick(s)}}},[e("div",{class:{"group-base":!0,"group-t":"yes"===s.status,"group-f":"no"===s.status}}),e("div",{staticClass:"list-content"},[e("div",{staticClass:"item-info"},[e("div",{class:{"item-title-f":"no"===s.status}},[t._v(t._s(s.name?s.name:t.name))]),e("div",{class:{"item-tips-f":"no"===s.status}},[t._v(t._s(s.createTimeStr))])]),e("div",{class:{"item-start":!0,"state-h":"yes"===s.status}},["yes"===s.status?e("div",[e("span",{staticStyle:{color:"#FF8F82"}},[t._v(t._s(s.highRiskCount))]),t._v("项高风险 "),e("img",{attrs:{src:i("2952"),alt:"",width:"100%",height:"100%"}})]):e("div",{staticStyle:{color:"#CCCCCC"}},[t._v("识别失败")])])])])})),0):e("div",{staticClass:"list-no"},[e("img",{staticClass:"no-data",attrs:{src:i("4c76"),alt:""}}),e("img",{staticClass:"no-data",attrs:{src:i("ba28"),alt:""}}),e("div",{staticClass:"list-no-t"},[t._v("抱歉，暂时没有体态评估的数据")])]),e("div",{staticClass:"nav-bottom"},[e("button",{staticClass:"btn",on:{click:t.clickBtn}},[t._v(" 开始体态评估 ")])])],1)},a=[],n=(i("b0c0"),i("aecd")),u=i("ed08"),c={name:"result_list",components:{VHead:n["a"]},data:function(){return{initResult:[],ut:"",name:"",userKey:""}},created:function(){this.ut=this.$route.query.ut||Object(u["b"])("ut"),this.name=this.$route.query.name||Object(u["b"])("name"),this.userKey=this.$route.query.userKey||Object(u["b"])("userKey"),this.initData()},methods:{initData:function(){var t=this,s={};this.userKey?s.userKey=this.userKey:s.code=this.ut,this.$axios({url:"/body/list",method:"GET",params:s}).then((function(s){t.initResult=s.data}))},itemClick:function(t){console.log(t),"no"!==t.status&&this.$router.push({path:"/result",query:{id:t.id}})},clickBtn:function(){this.$router.push({path:"/camera",query:{ut:this.ut,name:name,back:!0,userKey:this.userKey}})}},mounted:function(){}},o=c,r=(i("2502"),i("2877")),l=Object(r["a"])(o,e,a,!1,null,"3d073c7d",null);s["default"]=l.exports},b0c0:function(t,s,i){var e=i("83ab"),a=i("9bf2").f,n=Function.prototype,u=n.toString,c=/^\s*function ([^ (]*)/,o="name";!e||o in n||a(n,o,{configurable:!0,get:function(){try{return u.call(this).match(c)[1]}catch(t){return""}}})},ba28:function(t,s,i){t.exports=i.p+"static/img/no_data@2x.e6ec07b7.png"}}]);