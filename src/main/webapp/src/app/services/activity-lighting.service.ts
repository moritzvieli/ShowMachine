import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { ActivityLighting } from '../models/activity-lighting';
import { $WebSocket, WebSocketConfig } from 'angular2-websocket';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ActivityLightingService {

  public subject: Subject<ActivityLighting> = new Subject();

  // The websocket endpoint url
  private wsUrl: string;

  // The websocket connection
  websocket: $WebSocket;

  monitors: number = 0;

  constructor(private http: HttpClient
  ) {
    // Create the backend-url
    if (environment.name == 'dev') {
      this.wsUrl = 'ws://' + environment.localBackend + '/';
    } else {
      this.wsUrl = 'ws://' + window.location.hostname + ':' + window.location.port + '/';
    }

    this.wsUrl += 'api/activity/lighting';
  }

  startMonitor() {
    this.monitors ++;

    if(!this.websocket) {
    // Connect to the websocket backend
    const wsConfig = { reconnectIfNotNormalClose: true } as WebSocketConfig;
    this.websocket = new $WebSocket(this.wsUrl, null, wsConfig);

    this.websocket.onMessage(
      (msg: MessageEvent) => {
        this.subject.next(new ActivityLighting(JSON.parse(msg.data)));
      },
      { autoApply: false }
    );
    }
  }

  stopMonitor() {
    this.monitors --;

    if(this.monitors < 1 && this.websocket) {
      this.websocket.close();
      this.websocket = undefined;
    }
  }

}
