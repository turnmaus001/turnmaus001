(window.webpackJsonp=window.webpackJsonp||[]).push([[14],{358:function(e,t,r){var content=r(395);"string"==typeof content&&(content=[[e.i,content,""]]),content.locals&&(e.exports=content.locals);(0,r(20).default)("5111ac7d",content,!0,{sourceMap:!1})},393:function(e,t,r){e.exports=r.p+"img/81235ac.png"},394:function(e,t,r){"use strict";var n=r(358);r.n(n).a},395:function(e,t,r){(e.exports=r(19)(!1)).push([e.i,'@-webkit-keyframes spinAround{0%{-webkit-transform:rotate(0deg);transform:rotate(0deg)}to{-webkit-transform:rotate(359deg);transform:rotate(359deg)}}@keyframes spinAround{0%{-webkit-transform:rotate(0deg);transform:rotate(0deg)}to{-webkit-transform:rotate(359deg);transform:rotate(359deg)}}.c-vert-center{display:-webkit-box!important;display:flex!important;-webkit-box-align:center!important;align-items:center!important}.c-vert-center>:nth-child(2){margin-left:4px!important}.c-vert-inline-center{display:-webkit-inline-box!important;display:inline-flex!important;-webkit-box-align:center!important;align-items:center!important}.c-vert-inline-center>:nth-child(2){margin-left:4px!important}table tbody tr td,table thead tr td{vertical-align:middle!important}.banner{background:#fde8ff;border:1px solid #2575fc;-webkit-border-image:-webkit-gradient(linear,left top,right top,from(#833ab4),color-stop(50%,#fd1d1d),to(#fcb045)) 1;-webkit-border-image:linear-gradient(90deg,#833ab4 0,#fd1d1d 50%,#fcb045) 1;-o-border-image:linear-gradient(90deg,#833ab4 0,#fd1d1d 50%,#fcb045) 1;border-image:-webkit-gradient(linear,left top,right top,from(#833ab4),color-stop(50%,#fd1d1d),to(#fcb045)) 1;border-image:linear-gradient(90deg,#833ab4 0,#fd1d1d 50%,#fcb045) 1;display:-webkit-box;display:flex;-webkit-box-align:center;align-items:center;text-align:center;-webkit-box-pack:center;justify-content:center;position:relative;padding:5px;font-size:37px;font-style:italic}.banner-content{font-family:URW Chancery L,cursive;position:absolute;color:#833ab4}.columns{margin:0;-webkit-border-image:-webkit-gradient(linear,left top,right top,from(#833ab4),color-stop(50%,#fd1d1d),to(#fcb045)) 1;-webkit-border-image:linear-gradient(90deg,#833ab4 0,#fd1d1d 50%,#fcb045) 1;-o-border-image:linear-gradient(90deg,#833ab4 0,#fd1d1d 50%,#fcb045) 1;border-image:-webkit-gradient(linear,left top,right top,from(#833ab4),color-stop(50%,#fd1d1d),to(#fcb045)) 1;border-image:linear-gradient(90deg,#833ab4 0,#fd1d1d 50%,#fcb045) 1;border:1px solid #2575fc;border-top:none}.user-service,.user-waiting{padding:0!important}.user-service-header,.user-waiting-header{text-align:center;font-size:20px;position:relative}.user-service-header:after,.user-waiting-header:after{content:"";height:1px;background:linear-gradient(305deg,#67b945 28%,#ff9e38);bottom:0;width:53%;position:absolute;left:23%;right:30%}.user-service thead,.user-waiting thead{box-shadow:0 8px 6px -6px #000}.user-service thead th,.user-waiting thead th{border-width:0}.user-service .b-table .table,.user-waiting .b-table .table{border:none}.user-service tr.is-warning,.user-waiting tr.is-warning{background:linear-gradient(305deg,rgba(238,174,202,.2) 28%,rgba(148,187,233,.2));color:red}.user-waiting-header,.user-waiting thead{background:linear-gradient(305deg,#eeaeca 28%,#94bbe9)!important}.user-service{border-left:1px solid #e22b2b}.user-service-header,.user-service thead{background:linear-gradient(305deg,#94e9c0 28%,#eeaeca)!important}.user-service:before{content:"";position:absolute;height:100%;background-color:red}.table-wrapper{min-height:5rem}.turn-content{margin-bottom:auto}#myAudio,audio{display:none}',""])},428:function(e,t,r){"use strict";r.r(t);r(122),r(123),r(43),r(25);var n=r(26),o={layout:"user-management",data:function(){return{salonName:this.$t("services"),waiting:[],service:[],refresh:5,waitColumns:[{field:"name",label:this.$t("name")},{field:"waitTime",label:this.$t("waitting_time"),centered:!0},{field:"app",label:this.$t("app"),centered:!0}],serviceColumns:[{field:"name",label:this.$t("name")},{field:"time",label:this.$t("time_min"),centered:!0},{field:"employee",label:this.$t("employee"),centered:!0}]}},mounted:function(){this.getData(),this.autoCall()},methods:{getData:function(){var e,t,r,o,d,c,l;return regeneratorRuntime.async((function(m){for(;;)switch(m.prev=m.next){case 0:return m.prev=0,m.next=3,regeneratorRuntime.awrap(this.$axios.get(n.a.getWaitingList));case 3:(e=m.sent)&&e.data&&(t=this.waiting&&this.waiting.map((function(element){return element.name})),r=this.service&&this.service.map((function(element){return element.name})),o=e.data.waiting&&e.data.waiting.map((function(element){return element.name})),d=e.data.service&&e.data.service.map((function(element){return element.name})),c=JSON.stringify(t)!==JSON.stringify(o),l=JSON.stringify(r)!==JSON.stringify(d),(c||l)&&(this.$refs.Audio.play(),this.salonName=e.data.salonName,this.waiting=e.data.waiting,this.service=e.data.service,this.refresh=e.data.refresh)),m.next=10;break;case 7:m.prev=7,m.t0=m.catch(0),console.log("TCL: getCustomer -> error",m.t0);case 10:case"end":return m.stop()}}),null,this,[[0,7]])},autoCall:function(){var e=this;if(this.refresh){for(var t=[],i=0;i<60;)t.push(i),i+=this.refresh;this.intervalid=setInterval((function(){e.updateData(t)}),1e3)}},updateData:function(e){var time=(new Date).getSeconds();e.includes(time)&&this.getData()}}},d=(r(394),r(7)),component=Object(d.a)(o,(function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("section",{staticClass:"turn-content"},[n("div",{staticClass:"banner "},[n("audio",{ref:"Audio",attrs:{id:"myAudio",muted:"muted",autoplay:""}},[n("source",{attrs:{src:"http://nhacchuongvui.com/wp-content/uploads/Tieng-ting-www_nhacchuongvui_com.mp3?_=1",type:"audio/mpeg"}})]),e._v(" "),n("img",{staticClass:"banner-background",attrs:{src:r(393)}}),e._v(" "),n("div",{staticClass:"banner-content"},[e._v("\n      "+e._s(e.salonName)+"\n    ")])]),e._v(" "),n("div",{staticClass:"columns "},[n("div",{staticClass:"user-waiting column "},[n("div",{staticClass:"user-waiting-header"},[n("h1",[e._v(e._s(e.$t("waitting_customer")))])]),e._v(" "),n("b-table",{attrs:{data:e.waiting,columns:e.waitColumns,"row-class":function(e,t){return"YES"===e.warning&&"is-warning"}}})],1),e._v(" "),n("div",{staticClass:"user-service column "},[n("div",{staticClass:"user-service-header"},[n("h1",[e._v(e._s(e.$t("service_customer")))])]),e._v(" "),n("b-table",{attrs:{data:e.service,columns:e.serviceColumns}})],1)])])}),[],!1,null,null,null);t.default=component.exports}}]);