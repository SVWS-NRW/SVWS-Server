<script setup lang="ts">
	import { logEvent } from 'histoire/client';

	function onInput(event: Event) {
		logEvent('input', event);
	}
</script>

<template>
	<Story title="Text Input" id="svws-ui-text-input" icon="ri:pencil-line" :layout="{type: 'grid', width: '45%'}">
		<Variant title="Default" id="Default">
			<div class="py-4">
				<svws-ui-text-input type="text" model-value="" placeholder="Text Input Placeholder" valid @input="onInput" />
			</div>
		</Variant>

		<Variant title="Filled" id="Filled">
			<div class="py-4">
				<svws-ui-input-wrapper>
					<svws-ui-text-input type="text" model-value="Text Input Value" placeholder="Text Input Placeholder" valid @input="onInput" />
					<svws-ui-text-input type="email" model-value="buergermeister@bielefeld.de" placeholder="E-Mail-Adresse" @input="onInput" />
				</svws-ui-input-wrapper>
			</div>
		</Variant>

		<Variant title="Invalid" id="Invalid">
			<div class="py-4">
				<svws-ui-text-input type="text" model-value="Text Input Value" placeholder="Text Input Placeholder" :valid="false" @input="onInput" />
			</div>
		</Variant>

		<Variant title="Statistics" id="Statistics">
			<div class="py-4">
				<svws-ui-text-input type="text" model-value="" placeholder="Text Input Placeholder" valid statistics @input="onInput" />
			</div>
		</Variant>

		<Variant title="Disabled" id="Disabled">
			<div class="py-4">
				<svws-ui-text-input type="text" model-value="" placeholder="Text Input Placeholder" valid disabled @input="onInput" />
			</div>
		</Variant>

		<Variant title="Headless" id="Headless">
			<div class="bg-light p-4 font-bold">
				<svws-ui-text-input type="text" model-value="" placeholder="Text Input Placeholder" valid headless @input="onInput" />
			</div>
			<p class="mt-2 opacity-50">
				Info: Die Hintergrundfarbe des Headless Inputs ist transparent und zeigt so immer die Hintergrundfarbe des Parent-Elements.
				Mit einer spezifischen <code class="bg-black/25 rounded">bg-xxxx</code> Klasse kann die Hintergrundfarbe des Headless Inputs geändert werden.
			</p>
		</Variant>

		<Variant title="Maximal Length" id="Maximal Length">
			<div class="py-4">
				<svws-ui-text-input type="text" model-value="zu lang" placeholder="Text Input Placeholder" :max-len="5" @input="onInput" />
			</div>
		</Variant>

		<Variant title="Prefix" id="Prefix">
			<div class="py-4">
				<svws-ui-text-input type="text" model-value="" placeholder="Text Input Placeholder" valid url @input="onInput" />
			</div>
		</Variant>
	</Story>
</template>
