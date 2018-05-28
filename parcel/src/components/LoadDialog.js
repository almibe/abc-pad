import { h } from 'hyperapp'

export const LoadDialog = showDialog => state =>
  <div class={`modal ${state.showDialog ? "is-active" : ""}`}>
    <div class="modal-background"></div>
    <div class="modal-card">
      <header class="modal-card-head">
        <p class="modal-card-title">Manage Documents</p>
        <button class="delete" aria-label="close"></button>
      </header>
      <section class="modal-card-body">
        Content
      </section>
      <footer class="modal-card-foot">
        <button class="button is-success">Load</button>
        <button class="button">Cancel</button>
      </footer>
    </div>
  </div>
