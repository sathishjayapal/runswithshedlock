import htmx from 'htmx.org';
import 'css/app.css';


/**
 * Register an event at the document for the specified selector,
 * so events are still catched after DOM changes.
 */
function handleEvent(eventType: string, selector: string, handler: (event:Event) => any) {
  document.addEventListener(eventType, function(event) {
    const eventTarget = event.target! as Element;
    if (eventTarget.matches(selector + ', ' + selector + ' *')) {
      handler.apply(eventTarget.closest(selector), arguments);
    }
  });
}

handleEvent('change', '.js-selectlinks', function() {
  htmx.ajax('get', this.value, document.body);
  history.pushState({ htmx: true }, '', this.value);
});

handleEvent('click', 'body', function(event: Event) {
  // close any open dropdown
  const $clickedDropdown = (event.target! as Element).closest('.js-dropdown');
  const $dropdowns = document.querySelectorAll('.js-dropdown');
  $dropdowns.forEach(($dropdown) => {
    if ($clickedDropdown !== $dropdown && $dropdown.getAttribute('data-dropdown-keepopen') !== 'true') {
      $dropdown.ariaExpanded = 'false';
      $dropdown.nextElementSibling!.classList.add('hidden');
    }
  });
  // toggle selected if applicable
  if ($clickedDropdown) {
    $clickedDropdown.ariaExpanded = '' + ($clickedDropdown.ariaExpanded !== 'true');
    $clickedDropdown.nextElementSibling!.classList.toggle('hidden');
    event.preventDefault();
  }
});
