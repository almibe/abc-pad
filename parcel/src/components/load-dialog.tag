<load-dialog>
  <div class="modal">
    <div class="modal-background" onclick={hideLoad}></div>
    <div class="modal-card">
      <header class="modal-card-head">
        <p class="modal-card-title">Manage Documents</p>
        <button class="delete" aria-label="close" onclick={hideLoad}></button>
      </header>
      <section class="modal-card-body">
        <ul>
        </ul>
      </section>
    </div>
  </div>

  hideLoad() {
  }

</load-dialog>
