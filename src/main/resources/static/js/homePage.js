document.addEventListener("DOMContentLoaded", function () {
    const config = {
        step: 2,
        resizeDebounceTime: 300,
    };

    let position = 0;
    let totalWidth = 0;
    let resizeTimeout;
    const imageList = document.getElementById("imageList");
    const images = Array.from(imageList.children);

    function calculateTotalWidth() {
        totalWidth = images.reduce((acc, img) => acc + img.clientWidth, 0);
        console.log("Total Width Calculated:", totalWidth); // Debugging log
    }

    function cloneImages() {
        images.forEach((img) => {
            const clone = img.cloneNode(true);
            imageList.appendChild(clone);
        });
        console.log("Images cloned successfully"); // Debugging log
    }

    function moveImages() {
        position -= config.step;

        if (-position >= totalWidth) {
            position += totalWidth;
        }

        imageList.style.transform = `translateX(${position}px)`;
        console.log("Current Position:", position); // Debugging log
        requestAnimationFrame(moveImages);
    }

    function handleResize() {
        clearTimeout(resizeTimeout);
        resizeTimeout = setTimeout(() => {
            calculateTotalWidth();
        }, config.resizeDebounceTime);
    }

    calculateTotalWidth();
    cloneImages();

    window.addEventListener("resize", handleResize);

    requestAnimationFrame(moveImages);
});