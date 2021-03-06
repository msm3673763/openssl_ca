var classnames = require('classnames');

var getDotCount = function (spec) {
    var dots;
    dots = Math.ceil(spec.slideCount / spec.slidesToScroll);
    return dots;
};


var Dots = React.createClass({

    clickHandler: function (options, e) {
        // In Autoplay the focus stays on clicked button even after transition
        // to next slide. That only goes away by click somewhere outside
        e.preventDefault();
        this.props.clickHandler(options);
    },
    render: function () {

        var _this = this;

        var dotCount = getDotCount({
            slideCount: _this.props.slideCount,
            slidesToScroll: _this.props.slidesToScroll
        });

        // Apply join & split to Array to pre-fill it for IE8
        //
        // Credit: http://stackoverflow.com/a/13735425/1849458
        var dots = Array.apply(null, Array(dotCount + 1).join('0').split('')).map(function(x,i){

            var leftBound = (i * _this.props.slidesToScroll);
            var rightBound = (i * _this.props.slidesToScroll) + (_this.props.slidesToScroll - 1);
            var className = classnames({
                'ucs-slick-active': (_this.props.currentSlide >= leftBound) && (_this.props.currentSlide <= rightBound)
            });

            var dotOptions = {
                message: 'dots',
                index: i,
                slidesToScroll: _this.props.slidesToScroll,
                currentSlide: _this.props.currentSlide
            };

            var onClick = _this.clickHandler.bind(_this, dotOptions);

            return (
                <li key={i} className={className}>
                    {React.cloneElement(_this.props.customPaging(i), {onClick:onClick})}
                </li>
            );
        });

        return (
            <ul className={_this.props.dotsClass} style={{display: 'block'}}>
                {dots}
            </ul>
        );

    }
});

module.exports = Dots;
