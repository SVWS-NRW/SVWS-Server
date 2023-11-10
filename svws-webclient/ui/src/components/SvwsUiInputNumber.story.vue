<template>
	<Story title="Input Number" id="svws-ui-input-number" icon="ri:pencil-line" :layout="{type: 'grid', width: '45%'}">
		<Variant title="Default" id="Default">
			<div class="py-4">
				<svws-ui-input-wrapper>
					<svws-ui-input-number v-model="value1" placeholder="Nummer" @input="onInput" />
					<div>Wert: {{ value1 === null ? "null" : value1 }}</div>
				</svws-ui-input-wrapper>
			</div>
		</Variant>

		<Variant title="Default mit Minimum" id="DefaultMinimum">
			<div class="py-4">
				<svws-ui-input-wrapper>
					<svws-ui-input-number v-model="value2" placeholder="Nummer mit Minimum" min="0" @input="onInput" />
					<div>Wert: {{ value2 === null ? "null" : value2 }}</div>
				</svws-ui-input-wrapper>
			</div>
		</Variant>

		<Variant title="Invalid" id="Invalid">
			<div class="py-4">
				<svws-ui-input-number v-model="value3" placeholder="Nummer" :valid="(v) => false" @input="onInput" />
			</div>
			<div>Wert: {{ value3 === null ? "null" : value3 }}</div>
		</Variant>

		<Variant title="Statistics" id="Statistics">
			<div class="py-4">
				<svws-ui-input-number v-model="value3" placeholder="Nummer" statistics @input="onInput" />
			</div>
			<div>Wert: {{ value3 === null ? "null" : value3 }}</div>
		</Variant>

		<Variant title="Disabled" id="Disabled">
			<div class="py-4">
				<svws-ui-input-number v-model="value3" placeholder="Nummer" disabled @input="onInput" />
			</div>
			<div>Wert: {{ value3 === null ? "null" : value3 }}</div>
		</Variant>

		<Variant title="Headless" id="Headless">
			<div class="bg-light p-4 font-bold">
				<svws-ui-input-number v-model="value3" placeholder="Nummer" headless @input="onInput" />
			</div>
			<div>Wert: {{ value3 === null ? "null" : value3 }}</div>
			<p class="mt-2 opacity-50">
				Info: Die Hintergrundfarbe des Headless Inputs ist transparent und zeigt so immer die Hintergrundfarbe des Parent-Elements.
				Mit einer spezifischen <code class="bg-black/25 rounded">bg-xxxx</code> Klasse kann die Hintergrundfarbe des Headless Inputs geändert werden.
			</p>
		</Variant>
	</Story>
</template>

<script setup lang="ts">

	import { ref } from "vue";
	import { logEvent } from 'histoire/client';

	const value1 = ref<number | null>(1);
	const value2 = ref<number | null>(null);
	const value3 = ref<number | null>(42);

	function onInput(event: Event) {
		logEvent('input', event);
	}

</script>
