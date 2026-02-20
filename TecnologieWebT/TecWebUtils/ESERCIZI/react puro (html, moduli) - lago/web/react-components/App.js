'use strict';

class App extends React.Component {
  constructor(){
        super();

    this.state = {
        x: -1,
        y: -1,
        moves: -1,
        fishes: [],
        clicked: [],
        done: 0,
        total: 0
    };

    this.loadGame = this.loadGame.bind(this);
    this.onClick = this.onClick.bind(this);
    console.log('Settato state iniziale: ', this.state);
  }

  loadGame(event)
  {
    event.preventDefault();
    const x = parseInt(document.getElementById('x').value);
    const y = parseInt(document.getElementById('y').value);
    const moves = parseInt(document.getElementById('moves').value);

    let fishes = [];
    let clicked = [];
    for(let i = 0; i < x; i++)
    {
        fishes.push([]); //Genera la colonna x-esima
        clicked.push([]);
        for(let j = 0; j < y;j ++) //popola la colonna x-esima
        {
            fishes[i].push(Math.round(Math.random() * 5)); 
            clicked[i].push(false);
        }
    }

    if(this.state.moves > 0)//giochi successivi => reset griglia
    {
        for(let i = 1; i <= moves; i++)
        {
            document.getElementById(`move${i}`).innerText = '';
        }

        document.getElementById("total").innerHTML = '-';

        // Seleziona tutti i bottoni
        const buttons = document.querySelectorAll('button');

        // Filtra solo quelli che contengono una virgola
        const cells = Array.from(buttons).filter(button => button.id.includes(','));

        cells.map((b, index) => {
            console.log('Button ', b);
            b.disabled = false;
            b.innerHTML = '';
            b.style.backgroundColor = "lightgrey";
            b.style.cursor = 'pointer';
        })
    }

    console.log(fishes);
    this.setState({
        x: x,
        y: y,
        moves: moves,
        fishes: fishes,
        clicked: clicked,
        done: 0,
        total: 0
    }, () => {console.log('Stato aggiornato con i settings: ', this.state);})
                                               
    return false;
  }

  setButton(x, y, type)
  {
    const red = '#fc4343';
    const yellow = '#ffe134'
    const black = '#333';
    let element = document.getElementById(`${x},${y}`);
    if(element != null)
    {
        element.style.backgroundColor = (type === 'target' ? red : yellow);
        element.style.color = black;
        element.disabled = true;
        element.innerHTML = '<b>' + this.state.fishes[x][y] + '</b>';
    }
  }

  onClick(e) 
  {
    const red = '#fc4343';
    const yellow = '#ffe134'

    if(this.state.done === this.state.moves)
    {
        alert('Mosse esaurite, inizia una nuova partita per giocare ancora!');
        return;
    }
    let button = e.target.id; //id bottone 
    const coordinates = button.split(','); 
    const x = parseInt(coordinates[0]);
    const y = parseInt(coordinates[1]);
    console.log('Target: ', button, ' a (', x, ',', y, ')');
    
    //aggiornamento mosse
    this.setState({ done: this.state.done + 1}, () => {
        const currentScore = this.calculateMoveScore(x, y);
        document.getElementById('move' + this.state.done).innerHTML = `Move #` + this.state.done + `: <b>` + currentScore + `</b>`;
        this.setState({total: this.state.total + currentScore});

        if(this.state.done == this.state.moves) //mostra totale
        {
            document.getElementById('total').innerText = this.state.total + currentScore;
        }
    });
    
    //bottone target
    this.setButton(x, y, 'target');

    //buttons around target
    this.setButton(x - 1, y, '');
    this.setButton(x + 1, y, '');
    this.setButton(x, y - 1, '');
    this.setButton(x, y + 1, '');
};

calculateMoveScore(x, y)
{
    console.log('Calcolo punteggio', this.state.fishes, ' con cella target ', x, ', ', y)
    console.log('I limiti sono ', this.state.x, ', ', this.state.y);
    let tot = 0;
    let newClicked = this.state.clicked;

    tot += this.state.fishes[x][y];
    newClicked[x][y] = true;

    if(this.state.x > x + 1 && newClicked[x+1][y] === false)
    {
        tot += this.state.fishes[x+1][y];
        newClicked[x+1][y] = true;
    }
    if(x > 0 && newClicked[x-1][y] === false)
    {
        tot += this.state.fishes[x-1][y];
        newClicked[x-1][y] = true;
    }
    if(this.state.y > y + 1 && newClicked[x][y+1] === false)
    {
        tot += this.state.fishes[x][y+1];
        newClicked[x][y+1] = true;
    }
    if(y > 0 && newClicked[x][y-1] === false)
    {
        tot += this.state.fishes[x][y-1];
        newClicked[x][y-1] = true;
    }

    this.setState({ clicked: newClicked});

    return tot;
}

  render() {


    if(this.state.x == null)
        this.setState({x: -1});
    if(this.state.y == null)
        this.setState({y: -1});
    if(this.state.moves == null)
        this.setState({moves: 0});
    if(this.state.done == null)
        this.setState({done: 0});

      return (
		<>
			<h1>Benvenuto!</h1>
			<br />
            <form id="gameSettings" onSubmit={this.loadGame}>
                <h3>Settings</h3>
                <label >Width: </label>
                <input type="number" id="x" min="6" required />
                <br/>
                <label >Height: </label>
                <input type="number" id="y" min="6" required />
                <br/>
                <label>Moves: </label>
                <input type="number" id="moves" min="1" max="4" required />
                <br/>
                <input type="submit" value="Play!"/>
            </form>
            <br></br>
            <div className="container" id="game">
              <h1>LAKE GAME</h1>
              <div className="left">
                  <Lake x={this.state.x} y={this.state.y} onClick={this.onClick} />
              </div>
              <div className="right">
                    <Scoreboard moves={this.state.moves} />
              </div>
          </div>
        </>);
  }
}