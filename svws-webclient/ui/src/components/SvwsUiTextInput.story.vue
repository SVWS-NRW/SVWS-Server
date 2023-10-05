<template>
	<Story title="Text Input" id="svws-ui-text-input" icon="ri:pencil-line" :layout="{type: 'grid', width: '45%'}">
		<Variant title="Default" id="Default">
			<div class="py-4 flex flex-col gap-10">
				<svws-ui-text-input type="text" model-value="" placeholder="Text Input Placeholder" @input="onInput" />
				<svws-ui-text-input type="text" model-value="Text Input Value" placeholder="Text Input Placeholder" @input="onInput" />
			</div>
		</Variant>

		<Variant title="Type: Number" id="Number">
			<div class="py-4">
				<svws-ui-input-wrapper>
					<svws-ui-text-input type="number" model-value="1" placeholder="Nummer" @input="onInput" />
					<svws-ui-text-input type="number" model-value="" placeholder="Nummer mit Minimum" min="0" @input="onInput" />
				</svws-ui-input-wrapper>
			</div>
		</Variant>

		<Variant title="Special Types" id="Types">
			<div class="py-4">
				<svws-ui-input-wrapper>
					<svws-ui-text-input type="date" model-value="2023-10-05" placeholder="date" @input="onInput" />
					<svws-ui-text-input type="email" model-value="buergermeister@bielefeld.de" placeholder="email" @input="onInput" />
					<svws-ui-text-input type="tel" model-value="01234-774359" placeholder="tel" @input="onInput" />
				</svws-ui-input-wrapper>
			</div>
		</Variant>

		<Variant title="Invalid" id="Invalid">
			<div class="py-4">
				<svws-ui-text-input type="text" model-value="Text Input Value" placeholder="Text Input Placeholder" :valid="(v)=>false" @input="onInput" />
			</div>
		</Variant>

		<Variant title="Statistics" id="Statistics">
			<div class="py-4">
				<svws-ui-text-input type="text" model-value="" placeholder="Text Input Placeholder" statistics @input="onInput" />
			</div>
		</Variant>

		<Variant title="Disabled" id="Disabled">
			<div class="py-4">
				<svws-ui-text-input type="text" model-value="" placeholder="Text Input Placeholder" disabled @input="onInput" />
			</div>
		</Variant>

		<Variant title="Headless" id="Headless">
			<div class="bg-light p-4 font-bold">
				<svws-ui-text-input type="text" model-value="" placeholder="Text Input Placeholder" headless @input="onInput" />
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
				<svws-ui-text-input type="text" model-value="" placeholder="Text Input Placeholder" url @input="onInput" />
			</div>
		</Variant>
	</Story>
</template>

<script setup lang="ts">
	import { logEvent } from 'histoire/client';

	function onInput(event: Event) {
		logEvent('input', event);
	}
</script>
