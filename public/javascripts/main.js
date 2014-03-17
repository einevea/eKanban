function allowDrop(ev){
    ev.preventDefault();
}

function drag(ev){
    ev.dataTransfer.effectAllowed='move';
    ev.dataTransfer.setData("Text",ev.target.id);
}

function drop(ev, id){
    ev.preventDefault();
    var data = ev.dataTransfer.getData("Text");
    document.getElementById(id).appendChild(document.getElementById(data));
    ev.stopPropagation();
}