var Input = UcsmyUI.Input;
var Button = UcsmyUI.Button;
var Form = UcsmyUI.Form;
var FormItem = UcsmyUI.Form.FormItem;
var SelectDropDown = UcsmyUI.SelectDropDown;

var formData = {
    "domainName": [
        {type: "required", msg: "域名不能为空"},
        {type : "rule", rule : /^\S+$/, msg : "域名不能输入空格"},
        {type : "maxlength", maxlength : 50, msg : "域名长度不能超过50"}
    ],
    "city": [
        {type : "rule", rule : /^\S+$/, msg : "城市不能输入空格"},
        {type : "maxlength", maxlength : 50, msg : "城市长度不能超过50"}
    ],
    "orgUnitName": [
        {type : "rule", rule : /^\S+$/, msg : "组织单位名不能输入空格"},
        {type : "maxlength", maxlength : 100, msg : "组织单位名长度不能超过100"}
    ],
    "email": [
        {type : "rule", rule : /^$|^[\.a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/, msg : "邮箱输入格式错误"},
        {type : "maxlength", maxlength : 50, msg : "邮箱长度不能超过50"}
    ]

};

var validityOption = [
    {option: '一年', value: '365'},
    {option: '三年', value: '1095'},
    {option: '五年', value: '1825'},
    {option: '十年', value: '3650'},
    {option: '二十年', value: '7300'},
];

module.exports = React.createClass({
    getInitialState: function() {
        return {
            cerType: "",
            successFn: function(){}
        }
    },
    componentDidMount: function() {

    },
    init: function(type, successFn) {
        this.setState({
            cerType: type,
            successFn: successFn
        });
    },
    _return: function(event) {
        UcsmyIndex.closeChildrenPage();
    },
    _validate: function (callback) {
        var validateField = [
            "domainName", "city", "orgUnitName", "email"
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
                url : "cert/server",
                type:'post',
                async:true,
                data : $('#saveForm').serialize(),
                dataType: "json",
                success:function(data){
                    if (data.res == 1) {
                        UcsmyIndex.alert("提示", data.des);
                        UcsmyIndex.closeChildrenPage();
                        me.state.successFn();
                    } else {
                        UcsmyIndex.alert("错误", data.des);
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
                            <input type="hidden" name="cerType" value={this.state.cerType} />
                            <FormItem label="域名"><Input name="domainName"  /></FormItem>
                            <FormItem label="城市"><Input name="city"  /></FormItem>
                            <FormItem label="组织单位名"><Input name="orgUnitName"/></FormItem>
                            <FormItem label="邮箱"><Input name="email"/></FormItem>
                            <FormItem label="机器码"><Input name="machineCode"/></FormItem>
                            <FormItem label="有效期">
                                <SelectDropDown name="validity" defaultText="请选择" defaultValue="" ref="validity" value={validityOption.value} option={validityOption} searchPlaceholder="请选择" />
                            </FormItem>
                        </Form>
                    </div>
                </div>
                <div className="btn-panel">
                    <Button buttonType="bidnow" onClick={this._save}>保存</Button>
                    <Button buttonType="cancel" onClick={this._return}>取消</Button>
                </div>
            </div>
        );
    }
});