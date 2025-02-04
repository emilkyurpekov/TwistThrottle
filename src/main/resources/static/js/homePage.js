document.addEventListener("DOMContentLoaded", function () {
    const config = {
        step: 4,
        resizeDebounceTime: 300,
    };

    const imageList = document.getElementById("imageList");
    const container = imageList.parentElement;
    const images = Array.from(imageList.children);

    let imageWidths = [];
    let totalWidth = 0;
    let currentTranslateX = 0;

    function calculateWidths() {
        imageWidths = images.map((img) => img.offsetWidth + 10);
        totalWidth = imageWidths.reduce((sum, width) => sum + width, 0);


        const containerWidth = container.offsetWidth;
        while (totalWidth < containerWidth * 2) {
            images.forEach((img) => {
                const clone = img.cloneNode(true);
                imageList.appendChild(clone);
                images.push(clone);
                imageWidths.push(clone.offsetWidth + 10);
                totalWidth += clone.offsetWidth + 10;
            });
        }

        imageList.style.width = `${totalWidth}px`;
    }

    function moveImages() {
        currentTranslateX -= config.step;

        if (Math.abs(currentTranslateX) >= totalWidth / 2) {
            currentTranslateX = currentTranslateX % (totalWidth / 2);
        }

        imageList.style.transform = `translateX(${currentTranslateX}px)`;

        requestAnimationFrame(moveImages);
    }

    function handleResize() {
        calculateWidths();
        currentTranslateX = 0;
    }

    calculateWidths();
    requestAnimationFrame(moveImages);

    window.addEventListener("resize", handleResize);
});
