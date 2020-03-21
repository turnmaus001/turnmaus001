(window.webpackJsonp=window.webpackJsonp||[]).push([[4],{423:function(e,t,n){"use strict";n.r(t);n(43),n(24);var r=n(20),o={props:{open:{type:Boolean,required:!0},data:{type:Object,default:function(){return{}}}},data:function(){return{isOpen:this.open,form:this.data}},watch:{isOpen:function(e){this.$emit("update:open",e)},open:function(e){this.isOpen=e},data:function(e){this.form=e}},methods:{close:function(){this.$refs.modalRef.close()},save:function(){this.$emit("save",this.form),this.close()}}},l=n(7),c=Object(l.a)(o,(function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("div",{staticClass:"wrapper"},[n("b-modal",{ref:"modalRef",attrs:{active:e.isOpen,"has-modal-card":"","trap-focus":"","aria-role":"dialog","aria-modal":""},on:{"update:active":function(t){e.isOpen=t}}},[n("div",{staticClass:"login-modal modal-card",staticStyle:{width:"auto","min-width":"480px"}},[n("header",{staticClass:"modal-card-head"},[n("p",{staticClass:"modal-card-title"},[e._v(e._s(e.$t("category")))])]),e._v(" "),n("section",{staticClass:"modal-card-body"},[n("validation-observer",{ref:"form",attrs:{tag:"div"}},[n("div",{staticClass:"columns is-multiline"},[n("div",{staticClass:"column is-12"},[n("validation-provider",{attrs:{name:e.$t("category_name"),rules:"required"},scopedSlots:e._u([{key:"default",fn:function(t){var r=t.errors,o=t.failed;return[n("b-field",{attrs:{label:e.$t("category_name"),"label-position":"on-border",type:o?"is-danger":"",message:r[0]}},[n("b-input",{attrs:{placeholder:e.$t("category_name")},nativeOn:{keyup:function(t){return!t.type.indexOf("key")&&e._k(t.keyCode,"enter",13,t.key,"Enter")?null:e.save(t)}},model:{value:e.form.name,callback:function(t){e.$set(e.form,"name",t)},expression:"form.name"}})],1)]}}])})],1)])])],1),e._v(" "),n("footer",{staticClass:"modal-card-foot"},[n("b-button",{on:{click:e.close}},[e._v(e._s(e.$t("cancel")))]),e._v(" "),n("b-button",{attrs:{type:"is-primary"},on:{click:e.save}},[e._v(e._s(e.$t("save")))])],1)])])],1)}),[],!1,null,null,null).exports,d=(n(53),n(30),n(25),n(14),n(34),n(27)),f=(n(189),n(124)),m=n.n(f);function v(object,e){var t=Object.keys(object);if(Object.getOwnPropertySymbols){var n=Object.getOwnPropertySymbols(object);e&&(n=n.filter((function(e){return Object.getOwnPropertyDescriptor(object,e).enumerable}))),t.push.apply(t,n)}return t}function h(e){for(var i=1;i<arguments.length;i++){var source=null!=arguments[i]?arguments[i]:{};i%2?v(Object(source),!0).forEach((function(t){Object(d.a)(e,t,source[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(source)):v(Object(source)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(source,t))}))}return e}var y={name:null,s_name:null,money:null,time:null},_={props:{categoryId:{type:[String,Number],default:function(){return null}},open:{type:Boolean,required:!0},data:{type:Array,default:function(){return[{}]}}},data:function(){return{isOpen:this.open,form:this.data,enabled:[]}},computed:{columns:function(){return[{field:"name",label:this.$t("service_name"),centered:!0},{field:"s_name",label:this.$t("short_name"),centered:!0},{field:"money",label:this.$t("money_label")+"($)",centered:!0},{field:"time",label:this.$t("time")+"("+this.$t("min")+")",centered:!0},{field:"action",label:this.$t("action"),width:200,centered:!0}]}},watch:{isOpen:function(e){this.$emit("update:open",e)},open:function(e){this.isOpen=e},data:function(e){this.form=e||[h({},y)],this.enabled=this.generateEnabled(e||[h({},y)])}},methods:{close:function(){this.$emit("update"),this.$refs.modalRef.close()},add:function(){var form=this.form;form.push(m()(y)),this.enabled=this.generateEnabled(form),this.form=form},save:function(e){var t,param,n;return regeneratorRuntime.async((function(o){for(;;)switch(o.prev=o.next){case 0:return o.prev=0,t=r.a.addService,e.id&&(t=r.a.updateService),param={ser_id:this.categoryId,id:e.id,name:e.name,s_name:e.s_name,money:e.money,time:e.time},o.next=6,regeneratorRuntime.awrap(this.$axios.post(t,param));case 6:(n=o.sent).data&&n.data[this.$API.STATUS]===this.$API.OK?(this.$buefy.toast.open({message:this.$t("messages.save_success"),type:"is-success"}),this.$emit("save")):this.$buefy.toast.open({message:this.$t("messages.save_fail"),type:"is-danger"}),o.next=14;break;case 10:o.prev=10,o.t0=o.catch(0),console.log("TCL: save -> error",o.t0),this.$buefy.toast.open({message:this.$t("messages.save_fail"),type:"is-danger"});case 14:case"end":return o.stop()}}),null,this,[[0,10]])},edit:function(e,t){var n=m()(this.enabled);n[t]=!0,this.enabled=n},cancel:function(e,t){var n=m()(this.enabled);n[t]=!1,this.enabled=n,e.id||this.removeLocal(t)},remove:function(e,t){var n;return regeneratorRuntime.async((function(o){for(;;)switch(o.prev=o.next){case 0:if(o.prev=0,e.id){o.next=4;break}return this.removeLocal(t),o.abrupt("return");case 4:return o.next=6,regeneratorRuntime.awrap(this.$axios.post(r.a.deleteService,{id:e.id}));case 6:(n=o.sent).data&&n.data[this.$API.STATUS]===this.$API.OK?(this.removeLocal(t),this.$buefy.toast.open({message:this.$t("messages.delete_success"),type:"is-success"})):this.$buefy.toast.open({message:this.$t("messages.delete_fail"),type:"is-danger"}),o.next=14;break;case 10:o.prev=10,o.t0=o.catch(0),console.log("TCL: remove -> error",o.t0),this.$buefy.toast.open({message:this.$t("messages.delete_fail"),type:"is-danger"});case 14:case"end":return o.stop()}}),null,this,[[0,10]])},removeLocal:function(e){var form=m()(this.form);form.splice(e,1),this.enabled=this.generateEnabled(form),this.form=form},generateEnabled:function(data){return data.map((function(e){return!e.id}))}}},$={components:{CategoryDialog:c,ServiceDialog:Object(l.a)(_,(function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("div",{staticClass:"wrapper"},[n("b-modal",{ref:"modalRef",attrs:{active:e.isOpen,"has-modal-card":"","trap-focus":"","aria-role":"dialog","aria-modal":""},on:{"update:active":function(t){e.isOpen=t}}},[n("div",{staticClass:"login-modal modal-card",staticStyle:{width:"auto","min-width":"480px"}},[n("header",{staticClass:"modal-card-head"},[n("p",{staticClass:"modal-card-title"},[e._v(e._s(e.$t("service")))])]),e._v(" "),n("section",{staticClass:"modal-card-body"},[n("validation-observer",{ref:"form",attrs:{tag:"div"}},[n("b-table",{attrs:{data:e.form},scopedSlots:e._u([{key:"default",fn:function(t){return e._l(e.columns,(function(r){return n("b-table-column",{key:r.field,attrs:{field:r.field,label:r.label,width:r.width,centered:r.centered}},["money"===r.field?n("div",[n("validation-provider",{attrs:{name:r.label,rules:{required:!0,money:!0}},scopedSlots:e._u([{key:"default",fn:function(o){var l=o.errors,c=o.failed;return[n("b-field",{attrs:{type:c?"is-danger":"",message:l[0]}},[n("b-input",{attrs:{disabled:!e.enabled[t.index]},model:{value:e.form[t.index][r.field],callback:function(n){e.$set(e.form[t.index],r.field,n)},expression:"form[props.index][item.field]"}})],1)]}}],null,!0)})],1):"time"===r.field?n("div",[n("validation-provider",{attrs:{name:r.label,rules:{required:!0,integer:!0}},scopedSlots:e._u([{key:"default",fn:function(o){var l=o.errors,c=o.failed;return[n("b-field",{attrs:{type:c?"is-danger":"",message:l[0]}},[n("b-input",{attrs:{disabled:!e.enabled[t.index]},model:{value:e.form[t.index][r.field],callback:function(n){e.$set(e.form[t.index],r.field,n)},expression:"form[props.index][item.field]"}})],1)]}}],null,!0)})],1):"action"===r.field?n("div",[e.enabled[t.index]?e._e():n("b-button",{attrs:{type:"is-primary"},on:{click:function(n){return e.edit(t.row,t.index)}}},[e._v(e._s(e.$t("edit")))]),e._v(" "),e.enabled[t.index]?e._e():n("b-button",{attrs:{type:"is-danger"},on:{click:function(n){return e.remove(t.row,t.index)}}},[e._v(e._s(e.$t("delete")))]),e._v(" "),e.enabled[t.index]?n("b-button",{on:{click:function(n){return e.cancel(t.row,t.index)}}},[e._v(e._s(e.$t("cancel")))]):e._e(),e._v(" "),e.enabled[t.index]?n("b-button",{attrs:{type:"is-primary"},on:{click:function(n){return e.save(t.row,t.index)}}},[e._v(e._s(e.$t("save")))]):e._e()],1):n("div",[n("validation-provider",{attrs:{name:r.label,rules:"required"},scopedSlots:e._u([{key:"default",fn:function(o){var l=o.errors,c=o.failed;return[n("b-field",{attrs:{type:c?"is-danger":"",message:l[0]}},[n("b-input",{attrs:{disabled:!e.enabled[t.index]},model:{value:e.form[t.index][r.field],callback:function(n){e.$set(e.form[t.index],r.field,n)},expression:"form[props.index][item.field]"}})],1)]}}],null,!0)})],1)])}))}}])})],1),e._v(" "),n("b-button",{attrs:{type:"is-primary"},on:{click:e.add}},[e._v(e._s(e.$t("add_service")))])],1),e._v(" "),n("footer",{staticClass:"modal-card-foot"},[n("b-button",{on:{click:e.close}},[e._v(e._s(e.$t("close")))])],1)])])],1)}),[],!1,null,null,null).exports},data:function(){return{isOpenCategoryDialog:!1,categoryDialogData:null,selectedCategory:null,isOpenServiceDialog:!1,serviceDialogData:null}},computed:{rows:function(){return this.data},columns:function(){return[{field:"ser_id",label:this.$t("no"),numeric:!0},{field:"name",label:this.$t("category_name"),centered:!0},{field:"action",label:this.$t("action"),centered:!0}]}},asyncData:function(e){var t,n;return regeneratorRuntime.async((function(o){for(;;)switch(o.prev=o.next){case 0:return t=e.$axios,o.prev=1,o.next=4,regeneratorRuntime.awrap(t.get(r.a.getListService));case 4:if(!(n=o.sent).data){o.next=7;break}return o.abrupt("return",{data:n.data});case 7:return o.abrupt("return",{data:[]});case 10:return o.prev=10,o.t0=o.catch(1),console.log("TCL: Data -> error",o.t0),o.abrupt("return",{data:[]});case 14:case"end":return o.stop()}}),null,null,[[1,10]])},methods:{refreshService:function(){var e,t=this;return regeneratorRuntime.async((function(n){for(;;)switch(n.prev=n.next){case 0:return n.prev=0,n.next=3,regeneratorRuntime.awrap(this.$axios.get(r.a.getListService));case 3:(e=n.sent).data&&(this.data=e.data,this.serviceDialogData=e.data.filter((function(e){return e.ser_id===t.selectedCategory}))[0].service),n.next=10;break;case 7:n.prev=7,n.t0=n.catch(0),console.log("TCL: refreshService -> error",n.t0);case 10:case"end":return n.stop()}}),null,this,[[0,7]])},addInline:function(){this.isOpenCategoryDialog=!0,this.categoryDialogData={}},editInline:function(e){this.categoryDialogData={id:e.ser_id,name:e.name},this.isOpenCategoryDialog=!0},removeInline:function(e){var t;return regeneratorRuntime.async((function(n){for(;;)switch(n.prev=n.next){case 0:return n.prev=0,n.next=3,regeneratorRuntime.awrap(this.$axios.post(r.a.deleteCategory,{id:e.ser_id}));case 3:(t=n.sent).data&&t.data[this.$API.STATUS]===this.$API.OK?this.$buefy.toast.open({message:this.$t("messages.delete_success"),type:"is-success"}):this.$buefy.toast.open({message:this.$t("messages.delete_fail"),type:"is-danger"}),n.next=11;break;case 7:n.prev=7,n.t0=n.catch(0),console.log("TCL: removeInline -> error",n.t0),this.$buefy.toast.open({message:this.$t("messages.delete_fail"),type:"is-danger"});case 11:case"end":return n.stop()}}),null,this,[[0,7]])},saveCategory:function(form){var e,t;return regeneratorRuntime.async((function(n){for(;;)switch(n.prev=n.next){case 0:return n.prev=0,e=r.a.addCategory,form.id&&r.a.updateCategory,n.next=5,regeneratorRuntime.awrap(this.$axios.post(e,form));case 5:(t=n.sent).data&&t.data[this.$API.STATUS]===this.$API.OK?this.$buefy.toast.open({message:this.$t("messages.save_success"),type:"is-success"}):this.$buefy.toast.open({message:this.$t("messages.save_fail"),type:"is-danger"}),n.next=13;break;case 9:n.prev=9,n.t0=n.catch(0),console.log("TCL: saveCategory -> error",n.t0),this.$buefy.toast.open({message:this.$t("messages.save_fail"),type:"is-danger"});case 13:case"end":return n.stop()}}),null,this,[[0,9]])},updateService:function(e){this.selectedCategory=e.ser_id,this.serviceDialogData=m()(e.service),this.isOpenServiceDialog=!0}}},x=Object(l.a)($,(function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("div",{staticClass:"wrapper"},[n("h2",{staticClass:"title is-size-3 has-text-weight-bold has-text-primary"},[e._v(e._s(e.$t("category")))]),e._v(" "),n("div",{staticClass:"has-text-right"},[n("b-button",{attrs:{type:"is-primary"},on:{click:e.addInline}},[e._v(e._s(e.$t("add_category")))])],1),e._v(" "),n("div",{staticClass:"result"},[n("b-table",{attrs:{data:e.rows},scopedSlots:e._u([{key:"default",fn:function(t){return e._l(e.columns,(function(r){return n("b-table-column",{key:r.field,attrs:{field:r.field,label:r.label}},["action"===r.field?n("div",[n("b-button",{attrs:{type:"is-primary",size:"is-small",rounded:"","icon-left":"cogs"},on:{click:function(n){return e.updateService(t.row)}}},[e._v(e._s(e.$t("service")))]),e._v(" "),n("b-button",{attrs:{type:"is-primary",size:"is-small",rounded:"","icon-left":"square-edit-outline"},on:{click:function(n){return e.editInline(t.row)}}},[e._v(e._s(e.$t("edit")))]),e._v(" "),n("b-button",{attrs:{type:"is-danger",size:"is-small",rounded:"","icon-left":"delete"},on:{click:function(n){return e.removeInline(t.row)}}},[e._v(e._s(e.$t("delete")))])],1):n("div",[e._v(e._s(t.row[r.field]))])])}))}}])})],1),e._v(" "),n("category-dialog",{attrs:{open:e.isOpenCategoryDialog,data:e.categoryDialogData},on:{"update:open":function(t){e.isOpenCategoryDialog=t},save:e.saveCategory}}),e._v(" "),n("service-dialog",{attrs:{"category-id":e.selectedCategory,data:e.serviceDialogData,open:e.isOpenServiceDialog},on:{"update:open":function(t){e.isOpenServiceDialog=t},save:e.refreshService}})],1)}),[],!1,null,null,null);t.default=x.exports}}]);