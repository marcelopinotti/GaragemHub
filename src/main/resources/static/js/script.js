(function () {
    const root = document.documentElement;
    const page = document.getElementById('page');
    if (!page) return;

    const set = (x, y) => {
        root.style.setProperty('--mx-px', x + 'px');
        root.style.setProperty('--my-px', y + 'px');
    };

    // Inicia no centro da viewport
    set(window.innerWidth * 0.5, window.innerHeight * 0.3);

    window.addEventListener('mousemove', (e) => set(e.clientX, e.clientY), { passive: true });
})();
