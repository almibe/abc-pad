import { h } from 'hyperapp'

export const ABCEditor = ({ }) => (
  <div class="container">
    <div class="columns">
      <div class="column">
        <textarea id="abcEditor" class="textarea code" rows="20"></textarea>
        <div id="warnings"></div>
      </div>
      <div className="column">
        <div id="canvas"></div>
      </div>
    </div>
  </div>
)
