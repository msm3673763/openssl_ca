var Main = require('./other/main');

myPanel = React.createClass({
    getInitialState:function () {
        return{
        }
    },
    render: function() {
        return (
            <Main type="1" name="应用证书管理" />
        );
    }
});