# play-scala26-polymer3
Minimal play framework scala 2.6 application with polymer 3 for rendering views

## How to use ?

First launch sbt

### Full stack
* uiInit
* uiDeploy
* run

### ui only
* uiInit
* uiRun

## Example with REST
### Call rest api from a web component
First, add the following web components:  
> cd ui <br/>
> npm install @polymer/iron-ajax --save  
> npm install @polymer/paper-button --save  

Modify ui/src/my-view1.js:

```javascript
import { PolymerElement, html } from '@polymer/polymer/polymer-element.js';
import './shared-styles.js';
import '@polymer/iron-ajax/iron-ajax.js';
import '@polymer/paper-button/paper-button.js';

class MyView1 extends PolymerElement {

  getQuote() {
	  this.$.getQuoteAjax.generateRequest();
  }

  static get template() {
    return html`
      <style include="shared-styles">
        :host {
          display: block;

          padding: 10px;
        }
      </style>

		  

      <div class="card">
        <div class="circle">1</div>
        <h1>View One</h1>
	<blockquote>[[quote]]</blockquote>
        <p>Ut labores minimum atomorum pro. Laudem tibique ut has.</p>
        <p>Lorem ipsum dolor sit amet, per in nusquam nominavi periculis, sit elit oportere ea.Lorem ipsum dolor sit amet, per in nusquam nominavi periculis, sit elit oportere ea.Cu mei vide viris gloriatur, at populo eripuit sit.</p>

	<iron-ajax
	  id="getQuoteAjax"
	  auto
	  url="/api/test"
	  method="get"
	  handle-as="json"
	  last-response="{{quote}}">
	</iron-ajax>
	<paper-button raised on-tap="getQuote" class="primary">Get a New Quote</paper-button>
      </div>
    `;
  }
}

window.customElements.define('my-view1', MyView1);
```

In one terminal, start play server:
> sbt uiCleanAll <br/>
> sbt run

In antoher terminal, start polymer server with proxy enabled:
> cd ui <br/>
> polymer serve --proxy-target http://localhost:9000/api --proxy-path api

With your browser, go to:
> http://localhost:8081/
