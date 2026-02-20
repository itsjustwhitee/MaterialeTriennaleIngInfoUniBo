import React, { useState } from 'react';
import "./index.css";

const Mappa = ({ mappa, onCellClick }) => {
    return (

        <div>
            {mappa.map((row, rowIndex) => (
                <div key={rowIndex} className="row">
                    {row.map((cell, cellIndex) => (
                        <div key={cellIndex}
                            className={'cell ' + (cell === 'MINA' ? 'giallo' : cell)}
                            onClick={() => onCellClick(rowIndex, cellIndex)}
                        > {cell === 'rosso' && cell === mappa[rowIndex][cellIndex] ? 'MINA' : null}
                        </div>
                    ))}
                </div>
            ))}
        </div>
    );
};

export default Mappa;