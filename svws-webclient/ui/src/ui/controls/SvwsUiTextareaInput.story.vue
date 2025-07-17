<template>
	<Story title="Textarea Input" id="svws-ui-textarea-input" icon="ri:align-left" :layout="{type: 'grid', width: '45%'}">
		<Variant title="Default" id="Default">
			<div class="p-4">
				<svws-ui-input-wrapper>
					<svws-ui-textarea-input model-value="" placeholder="Textarea Input Placeholder" @input="onInput" />
					<svws-ui-textarea-input v-model="modelValue" placeholder="Textarea Input Placeholder" @input="onInput" />
				</svws-ui-input-wrapper>
			</div>
		</Variant>

		<Variant title="Resize props" id="Resize">
			<div class="p-4">
				<svws-ui-input-wrapper>
					<svws-ui-textarea-input v-model="modelValue" placeholder="resizeable: vertical (default)" resizeable="vertical" @input="onInput" />
					<svws-ui-textarea-input v-model="modelValue" placeholder="resizeable: horizontal" resizeable="horizontal" @input="onInput" />
					<svws-ui-textarea-input v-model="modelValue" placeholder="resizeable: both" resizeable="both" @input="onInput" />
					<svws-ui-textarea-input v-model="modelValue" placeholder="resizeable: none" resizeable="none" @input="onInput" />
					<svws-ui-textarea-input v-model="modelValue" placeholder="resizeable: none + autoresize" resizeable="none" autoresize @input="onInput" />
				</svws-ui-input-wrapper>
			</div>
		</Variant>

		<Variant title="Invalid/Required" id="Invalid">
			<div class="p-4 flex flex-col gap-10">
				<svws-ui-input-wrapper>
					<svws-ui-textarea-input placeholder="Invalid Input" :valid="(v)=>false" resizeable="vertical" @input="onInput" />
					<svws-ui-textarea-input v-model="modelValue" placeholder="Required Length Textarea" resizeable="vertical" @input="onInput" :max-len="10" />
				</svws-ui-input-wrapper>
				<svws-ui-input-wrapper>
					<svws-ui-textarea-input model-value="" placeholder="Required" resizeable="vertical" @input="onInput" required />
					<svws-ui-textarea-input model-value="Required Value" placeholder="Required Value" resizeable="vertical" @input="onInput" required />
				</svws-ui-input-wrapper>
			</div>
		</Variant>

		<Variant title="Disabled" id="Disabled">
			<div class="p-4">
				<svws-ui-textarea-input v-model="modelValue" disabled placeholder="Textarea Input Placeholder" resizeable="vertical" @input="onInput" />
			</div>
		</Variant>

		<Variant title="Statistics" id="Statistics">
			<div class="p-4">
				<svws-ui-textarea-input v-model="modelValue" statistics placeholder="Textarea Input Placeholder" resizeable="vertical" @input="onInput" />
			</div>
		</Variant>
		<Variant title="Headless" id="Headless">
			<div class="bg-ui-neutral px-4 py-8 font-bold mb-1">
				<svws-ui-textarea-input v-model="modelValue" headless placeholder="Textarea Input Placeholder" resizeable="vertical" @input="onInput" />
			</div>
			<div class="bg-ui-brand text-ui-onbrand px-4 py-8 font-bold">
				<svws-ui-textarea-input v-model="modelValue" headless resizeable="none" :rows="1" placeholder="Textarea Input Placeholder" @input="onInput" />
			</div>
			<p class="mt-2 text-ui-secondary">
				Info: Die Hintergrundfarbe des Headless Inputs ist transparent und zeigt so immer die Hintergrundfarbe des Parent-Elements.<br>
				Mit <code class="bg-ui-neutral px-2 rounded-xs">bg-ui-xxxx text-ui-xxxx</code> in einem übergeordneten Element kann die Hintergrundfarbe und Textfarbe des Inputs geändert werden.
			</p>
		</Variant>
	</Story>
</template>

<script setup lang="ts">

	import { ref } from 'vue';
	import { logEvent } from '../../stories/helper';

	const modelValue = ref("This is a filled textarea component");

	function onInput(value: string | null) {
		logEvent('input', value);
	}

</script>
