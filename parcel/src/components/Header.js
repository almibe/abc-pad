import { h } from 'hyperapp'

export const Header = ({ docId, docName }) => (
  <div class="container">
    <div class="container">
      <h1 class="title">ABC Editor</h1>
    </div>

    <div class="container">
      <div class="columns">
        <div class="column">
          <div class="field is-horizontal">
            <div class="field-label is-normal">
              <label class="label">Name</label>
            </div>
            <div class="field-body">
              <div class="field">
                <p class="control">
                  <input id="name" class="input" type="text" value={docName} />
                  <input id="id" class="input" type="hidden" value={docId} />
                </p>
              </div>
            </div>
          </div>
        </div>
        <div class="column">
          <div class="field is-grouped">
            <div class="control">
              <button class="button" id="save">Save</button>
            </div>
            <div class="control">
              <button class="button" id="showLoad">Load</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
)
