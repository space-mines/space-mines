let scene;
let camera;
let flyControls;
let renderer;
let minefield;
let mineMeshes = [];
let difficulty;
let mineCount;
let dimension;
let gameId;
let gameData;
let playerId;

function get(name){
    if(name=(new RegExp('[?&]'+encodeURIComponent(name)+'=([^&]*)')).exec(location.search))
        return decodeURIComponent(name[1]);
}

function createSpotlight() {
    let spotlight = new THREE.SpotLight(0xffffff);
    spotlight.position.set(50, 50, 100);
    spotlight.castShadow = true;
    return spotlight;
}

function createCameraLookingAt(position) {
    let camera = new THREE.PerspectiveCamera(45, window.innerWidth/window.innerHeight, 0.1, 1000);
    camera.position.set(50, 50, 125);
    camera.lookAt(position);

    flyControls = new THREE.FlyControls(camera);

    flyControls.movementSpeed = 25;
    flyControls.domElement = document.querySelector("#WebGL-output");
    flyControls.rollSpeed = Math.PI / 24;
    flyControls.autoForward = false;
    flyControls.dragToLook = true;

    return camera;
}

function createRenderer() {
    let renderer = new THREE.WebGLRenderer();
    renderer.setClearColor(0x444444, 1.0);
    renderer.setSize(window.innerWidth, window.innerHeight);
    renderer.shadowMapEnabled = true;

    return renderer;
}

let clock = new THREE.Clock();

function renderScene() {
    let delta = clock.getDelta();
    flyControls.update(delta);

    //scene.traverse(function(obj) {
    //    if(obj instanceof THREE.Mesh) {
    //        obj.animate();
    //    }
    //});

    requestAnimationFrame(renderScene);
    renderer.render(scene, camera);
}

function addMinefieldTo(scene) {
    minefield = Pod.createMinefield(gameData.sectors);
    for (let key in minefield.pods) {
        let mesh = minefield.pods[key].mesh;
        scene.add(mesh);
        mineMeshes.push(mesh);
    }
}

function updateMinefield(podData) {
    for(let i = 0; i < podData.length; ++i) {
        let data = podData[i];
        let key = Pod.getKey(data.x, data.y, data.z);
        let pod = minefield.pods[key];
        pod.data = data;
        Pod.update(pod);
    }

}

function init() {
    playerId = get("playerId");
    getGameData(playerId);
}

function getGameData(playerId) {
    let xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            gameData = JSON.parse(this.responseText);
            gameId = gameData.id;
            console.log("Game ID=" + gameId);
            startGame();
        }
    };
    xhttp.open("GET", "/game", true);
    xhttp.setRequestHeader("Content-type", "application/json");
    xhttp.send();
}

function markPod(podId) {
    let xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            gameData = JSON.parse(this.responseText);
            updateMinefield(gameData.sectors);
        }
    };
    xhttp.open("GET", "/game/mark?sectorId=" + podId, true);
    xhttp.setRequestHeader("Content-type", "application/json");
    xhttp.send();
}

function revealPod(podId) {
    let xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            gameData = JSON.parse(this.responseText);
            updateMinefield(gameData.sectors);
        }
    };
    xhttp.open("GET", "/game/reveal?sectorId=" + podId, true);
    xhttp.setRequestHeader("Content-type", "application/json");
    xhttp.send();
}

function startGame() {

    scene = new THREE.Scene();
    addMinefieldTo(scene);
    camera = createCameraLookingAt(Pod.getCenter(minefield));
    renderer = createRenderer();

    let spotlight = createSpotlight();
    let ambientLight = new THREE.AmbientLight(0x505050);


    scene.add(ambientLight);
    scene.add(camera);
    scene.add(spotlight);

    document.onmousedown = onMouseDown;

    document.getElementById("WebGL-output")
        .appendChild(renderer.domElement);

    renderScene(renderer, scene, camera);
}

function onMouseDown(event) {
    let vector = new THREE.Vector3();

    vector.set(
        ( event.clientX / window.innerWidth ) * 2 - 1,
        -( event.clientY / window.innerHeight ) * 2 + 1,
        0.5);

    vector.unproject(camera);

    let raycaster = new THREE.Raycaster(camera.position, vector.sub(camera.position).normalize());
    let intersects = raycaster.intersectObjects(mineMeshes);
    let selected;

    for(let i = 0; i < intersects.length; ++i) {
        selected = intersects[i].object;
        if(selected.visible) {
            let pod = Pod.findByMesh(minefield, selected);
            if(event.ctrlKey || event.button != 0) {
                markPod(pod.data.id);
            }
            else {
                revealPod(pod.data.id);
            }
            break;
        }
    }
}

function onResize() {
    camera.aspect = window.innerWidth / window.innerHeight;
    camera.updateProjectionMatrix();
    renderer.setSize(window.innerWidth, window.innerHeight);
}

window.addEventListener('resize', onResize, false);
window.onload = init;
