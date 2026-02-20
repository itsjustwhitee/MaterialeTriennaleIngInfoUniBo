import React, { useState } from 'react';
import "./index.css";

const Mappa = ({ mappa, onCellClick }) => {
    return (

        <div>
            {mappa.map((row, rowIndex) => (
                <div key={rowIndex} className="row">
                    {row.map((cell, cellIndex) => (
                        <div key={cellIndex}
                            className={'cell ' + (cell === 'T' ? 'giallo' : cell)}
                            onClick={() => onCellClick(rowIndex, cellIndex)}
                        > {cell === 'blu' && cell === mappa[rowIndex][cellIndex] ? 'T' : null}
                        </div>
                    ))}
                </div>
            ))}
        </div>
    );
};

export default Mappa;