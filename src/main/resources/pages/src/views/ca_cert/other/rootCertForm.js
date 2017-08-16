var Input = UcsmyUI.Input;
var Button = UcsmyUI.Button;
var Form = UcsmyUI.Form;
var FormItem = UcsmyUI.Form.FormItem;

var formData = {
    "domainName": [
        {type: "required", msg: "域名不能为空"},
        {type : "maxlength", maxlength : 50, msg : "域名长度不能超过50"}
    ],
    "country": [
        {type: "required", msg: "国家不能为空"},
        {type : "maxlength", maxlength : 2, msg : "国家长度不能超过2"}
    ],
    "province": [
        {type: "required", msg: "省份不能为空"},
        {type : "maxlength", maxlength : 50, msg : "省份长度不能超过50"}
    ],
    "city": [
        {type: "required", msg: "城市不能为空"},
        {type : "maxlength", maxlength : 50, msg : "城市长度不能超过50"}
    ],
    "orgName": [
        {type: "required", msg: "组织名不能为空"},
        {type : "maxlength", maxlength : 100, msg : "组织名长度不能超过100"}
    ],
    "orgUnitName": [
        {type: "required", msg: "组织名单位不能为空"},
        {type : "maxlength", maxlength : 100, msg : "组织单位名长度不能超过100"}
    ],
    "email": [
        {type : "rule", rule : /^$|^[\.a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/, msg : "邮箱输入格式错误"},
        {type : "maxlength", maxlength : 50, msg : "邮箱长度不能超过50"}
    ]

};

module.exports = React.createClass({
    getInitialState: function() {
        return {
            successFn: function(){}
        }
    },
    componentDidMount: function() {

    },
    init: function(successFn) {
        this.setState({
            successFn: successFn
        });
        UcsmyIndex.alert("提示", "创建根证书会把之前相关的证书都删除");
    },
    _return: function() {
        UcsmyIndex.closeChildrenPage();
    },
    _validate: function (callback) {
        var validateField = [
            "domainName", "country",
            "province", "city", "orgName",
            "orgUnitName", "email"
        ];
        var fn = function (result) {
            if(result) {
                callback();
            }
        };
        this.refs.saveForm.validate(fn, validateField);
    },
    _save: function() {
        var me = this;
        this._validate(function(){
            $.ajax({
                url : "cert/ca",
                type:'post',
                async:false,
                data : $('#saveForm').serialize(),
                dataType: "json",
                success:function(data){
                    var objData = data;
                    if(objData.retcode == 0){
                        UcsmyIndex.alert("提示", objData.retmsg);
                        UcsmyIndex.closeChildrenPage();
                        me.state.successFn();
                    }
                    else{
                        UcsmyIndex.alert("错误", objData.retmsg);
                    }
                },
                error:function(xhr, errorText, errorType){
                    UcsmyIndex.alert("错误", "网络异常，请稍后再试");
                }
            });
        });
    },
    render: function() {
        return (
            <div>
                <div className="panel">
                    <div className="panel-title fc-red">生成证书</div>
                    <div className="panel-content">
                        <Form ref="saveForm" formData={formData} id="saveForm">
                            <FormItem label="域名"><Input name="domainName"  /></FormItem>
                            <FormItem label="国家"><Input name="country"  /></FormItem>
                            <FormItem label="省份"><Input name="province"/></FormItem>
                            <FormItem label="城市"><Input name="city"  /></FormItem>
                            <FormItem label="组织名"><Input name="orgName"/></FormItem>
                            <FormItem label="组织单位名"><Input name="orgUnitName"/></FormItem>
                            <FormItem label="邮箱"><Input name="email"/></FormItem>
                        </Form>
                    </div>
                </div>
                <div className="btn-panel">
                    <Button buttonType="bidnow" onClick={this._save}>生成证书</Button>
                    <Button buttonType="cancel" onClick={this._return}>取消</Button>
                </div>
            </div>
        );
    }
});