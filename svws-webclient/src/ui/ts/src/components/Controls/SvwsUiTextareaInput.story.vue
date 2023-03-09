<script setup lang="ts">
	import { logEvent } from 'histoire/client';
	import { ref } from 'vue';

	const modelValue = ref("This is a filled textarea component");

	function onInput(event: Event) {
		logEvent('input', event);
	}
</script>

<template>
	<Story title="SVWS UI/Controls/Textarea Input">
		<Variant title="Empty">
			<svws-ui-textarea-input model-value="" valid placeholder="Textarea Input Placeholder" resizeable="both" @input="onInput" />
		</Variant>

		<Variant title="Filled">
			<svws-ui-textarea-input v-model="modelValue" valid placeholder="Textarea Input Placeholder" resizeable="both" @input="onInput" />
		</Variant>

		<Variant title="Invalid">
			<svws-ui-textarea-input v-model="modelValue" :valid="false" placeholder="Textarea Input Placeholder" resizeable="both" @input="onInput" />
		</Variant>

		<Variant title="Disabled">
			<svws-ui-textarea-input v-model="modelValue" valid disabled placeholder="Textarea Input Placeholder" resizeable="both" @input="onInput" />
		</Variant>

		<Variant title="Statistics">
			<svws-ui-textarea-input v-model="modelValue" valid statistics placeholder="Textarea Input Placeholder" resizeable="both" @input="onInput" />
		</Variant>

		<Variant title="Non-resizable">
			<svws-ui-textarea-input v-model="modelValue" valid placeholder="Textarea Input Placeholder" resizeable="none" @input="onInput" />
		</Variant>

		<Variant title="Autoresize">
			<svws-ui-textarea-input v-model="modelValue" placeholder="Textarea Input Placeholder" resizeable="none" autoresize @input="onInput" />
		</Variant>
	</Story>
</template>