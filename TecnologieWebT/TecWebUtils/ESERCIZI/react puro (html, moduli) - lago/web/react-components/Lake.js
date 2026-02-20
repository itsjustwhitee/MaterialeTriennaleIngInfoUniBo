'use strict';
class Lake extends React.Component {
    constructor(){
          super();
    }

    render(){

        if(this.props.x <= 0 || this.props.y <= 0)
            return <></>;

        let items = [];

        items.push([]);
        items[0].push(<td key="empty"></td>);

        for(let x = 0; x < this.props.x; x++) //stampa header dei numeri 
        {
            var name = `headerX${x}`;
            items[0].push(<td key={name}>{x}</td>);
        }
        for(let y = 0; y < this.props.y; y++) 
        {
            var name = `headerY${y}`;
            items.push([]);
            items[y+1].push(<td key={name}>{y}</td>);
            for (let x = 0; x < this.props.x; x++) {
                name = `${x},${y}`;
                items[y+1].push(<td key={name}><button id={name} onClick={this.props.onClick} style={{color:"lightgrey"}}></button></td>);
            }
        }

        items = (items.map((row, index) => <tr key={index}>{row}</tr>));

        return (<table id="grid" className="onDark"> {items} </table>);
    }
}