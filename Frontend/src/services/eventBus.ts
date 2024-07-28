type Callback = (data: any) => void;

class EventBus {
    private events: Record<string, Callback[]> = {};

    on(event: string, callback: Callback) {
        if (!this.events[event]) {
            this.events[event] = [];
        }
        this.events[event].push(callback);
    }

    off(event: string, callback: Callback) {
        if (!this.events[event]) return;

        this.events[event] = this.events[event].filter(cb => cb !== callback);
    }

    emit(event: string, data: any) {
        if (!this.events[event]) return;

        this.events[event].forEach(callback => callback(data));
    }
}

const eventBus = new EventBus();
export default eventBus;
