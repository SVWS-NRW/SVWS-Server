<template>
	<Story title="Textarea Input" id="svws-ui-textarea-input" icon="ri:align-left" :layout="{type: 'grid', width: '45%'}">
		<Variant title="Empty" id="Empty">
			<div class="py-4">
				<svws-ui-textarea-input model-value="" placeholder="Textarea Input Placeholder" resizeable="vertical" @input="onInput" />
			</div>
		</Variant>

		<Variant title="Filled" id="Filled">
			<div class="py-4">
				<svws-ui-textarea-input v-model="modelValue" placeholder="Textarea Input Placeholder" resizeable="vertical" @input="onInput" />
			</div>
		</Variant>

		<Variant title="Invalid" id="Invalid">
			<div class="py-4">
				<svws-ui-textarea-input v-model="modelValue" :valid="v=>false" placeholder="Textarea Input Placeholder" resizeable="vertical" @input="onInput" />
			</div>
		</Variant>

		<Variant title="Statistics" id="Statistics">
			<div class="py-4">
				<svws-ui-textarea-input v-model="modelValue" statistics placeholder="Textarea Input Placeholder" resizeable="vertical" @input="onInput" />
			</div>
		</Variant>

		<Variant title="Disabled" id="Disabled">
			<div class="py-4">
				<svws-ui-textarea-input v-model="modelValue" disabled placeholder="Textarea Input Placeholder" resizeable="vertical" @input="onInput" />
			</div>
		</Variant>

		<Variant title="Non-resizable" id="Non-resizable">
			<div class="py-4">
				<svws-ui-textarea-input v-model="modelValue" placeholder="Textarea Input Placeholder" resizeable="none" @input="onInput" />
			</div>
		</Variant>

		<Variant title="Autoresize" id="Autoresize">
			<div class="py-4">
				<svws-ui-textarea-input v-model="modelValue" placeholder="Textarea Input Placeholder" resizeable="none" autoresize @input="onInput" />
			</div>
		</Variant>
	</Story>
</template>

<script setup lang="ts">
	import { logEvent } from 'histoire/client';
	import { ref } from 'vue';

	const modelValue = ref("This is a filled textarea component");

	function onInput(event: Event) {
		logEvent('input', event);
	}
</script>
