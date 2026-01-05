export function runtimeFormated(runtime: number) {
    let hours = Math.floor(runtime / 60);
    let minutes = runtime % 60;
    return `${hours}h ${minutes}min`;
}