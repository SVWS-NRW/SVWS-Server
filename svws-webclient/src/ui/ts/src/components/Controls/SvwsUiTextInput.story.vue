<script setup lang="ts">
	import { logEvent } from 'histoire/client';

	function onInput(event: Event) {
		logEvent('input', event);
	}
</script>

<template>
	<Story title="SVWS UI/Controls/Text Input">
		<Variant title="Empty">
			<svws-ui-text-input type="text" model-value="" placeholder="Text Input Placeholder" valid @input="onInput" />
		</Variant>

		<Variant title="Filled">
			<svws-ui-text-input type="text" model-value="Text Input Value" placeholder="Text Input Placeholder" valid @input="onInput" />
		</Variant>

		<Variant title="Invalid">
			<svws-ui-text-input type="text" model-value="Text Input Value" placeholder="Text Input Placeholder" :valid="false" @input="onInput" />
		</Variant>

		<Variant title="eMail">
			<svws-ui-text-input type="email" model-value="buergermeister@bielefeld.de" placeholder="eMail-Adresse" @input="onInput" />
		</Variant>

		<Variant title="Disabled">
			<svws-ui-text-input type="text" model-value="" placeholder="Text Input Placeholder" valid disabled @input="onInput" />
		</Variant>

		<Variant title="Statistics">
			<svws-ui-text-input type="text" model-value="" placeholder="Text Input Placeholder" valid statistics @input="onInput" />
		</Variant>

		<Variant title="Prefix">
			<svws-ui-text-input type="text" model-value="" placeholder="Text Input Placeholder" valid url @input="onInput" />
		</Variant>

		<Variant title="MaxLen">
			<svws-ui-text-input type="text" model-value="zu lang" placeholder="Text Input Placeholder" :max-len="5" @input="onInput" />
		</Variant>

		<Variant title="Headless">
			<div class="bg-light p-8 font-bold">
				<svws-ui-text-input type="text" model-value="" placeholder="Text Input Placeholder" valid headless @input="onInput" class="bg-highlight" />
			</div>
			<p class="mt-2 italic">
				Hintergrundfarbe des Headless Inputs ist immer transparent und zeigt die Hintergrundfarbe des Parent-Elements an.
				<br>Mit einer spezifischen <code>bg-xyz</code> Klasse kann die Hintergrundfarbe des Headless Inputs geändert werden.
			</p>
		</Variant>
	</Story>
</template>
