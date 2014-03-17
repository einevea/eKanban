function allowDrop(ev){
    ev.preventDefault();
}

function drag(ev, storyId){
    ev.dataTransfer.effectAllowed='move';
    ev.dataTransfer.setData("Text",ev.target.id);
    ev.dataTransfer.setData("storyId", storyId);
}

function drop(ev, projectId, phaseId){
    ev.preventDefault();
    var data = ev.dataTransfer.getData("Text");
    var storyId = ev.dataTransfer.getData("storyId");
    $.get("/projects/"+projectId+"/stories/"+storyId+"/phases/"+phaseId+"/put");
    document.getElementById("phase_"+phaseId).appendChild(document.getElementById(data));
    ev.stopPropagation();
}