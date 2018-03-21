import { h } from 'hyperapp'

export const Header = ({saveDocument, showLoad}) => (
  <div class="container header">
    <div class="container">
      <div class="columns">
        <div class="column">
          <h1 class="title">ABC Editor</h1>
        </div>
        <div class="column">
          <div class="field is-grouped">
            <div class="control">
              <button class="button" id="save" onclick={() => saveDocument()}>Save</button>
            </div>
            <div class="control">
              <button class="button" id="showLoad" onclick={() => showLoad()}>Load</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
)
