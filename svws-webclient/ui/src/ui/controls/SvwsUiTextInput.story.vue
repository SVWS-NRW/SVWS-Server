<template>
	<Story title="Text Input" id="svws-ui-text-input" icon="ri:pencil-line" :layout="{type: 'grid', width: '45%'}">
		<Variant title="Default" id="Default">
			<div class="p-4">
				<svws-ui-input-wrapper>
					<svws-ui-text-input type="text" model-value="" placeholder="Placeholder" @input="onInput" />
					<svws-ui-text-input type="text" model-value="Text Input Value" placeholder="Placeholder" @input="onInput" />
				</svws-ui-input-wrapper>
			</div>
		</Variant>

		<Variant title="Special Types" id="Types">
			<div class="p-4 flex flex-col gap-10">
				<svws-ui-input-wrapper>
					<svws-ui-text-input type="date" model-value="2024-10-11" placeholder="type: date" @input="onInput" />
					<svws-ui-text-input type="date" model-value="2024-10-11" placeholder="type: date + removable" removable @input="onInput" />
					<svws-ui-text-input type="email" model-value="buergermeister@bielefeld.de" placeholder="type: email" @input="onInput" />
					<svws-ui-text-input type="tel" model-value="01234-774359" placeholder="type: tel" @input="onInput" />
				</svws-ui-input-wrapper>
				<svws-ui-input-wrapper>
					<svws-ui-text-input type="search" model-value="" placeholder="type: search" @input="onInput" />
					<svws-ui-text-input type="search" model-value="Search Value" placeholder="type: search" @input="onInput" />
				</svws-ui-input-wrapper>
			</div>
		</Variant>

		<Variant title="Invalid/Required" id="Invalid">
			<div class="p-4 flex flex-col gap-10">
				<svws-ui-input-wrapper>
					<svws-ui-text-input type="text" model-value="Text Input Value" placeholder="Invalid Input" :valid="(v)=>false" @input="onInput" />
				</svws-ui-input-wrapper>
				<svws-ui-input-wrapper>
					<svws-ui-text-input type="text" model-value="" placeholder="Required" required @input="onInput" />
					<svws-ui-text-input type="text" model-value="Required Value" placeholder="Required" required @input="onInput" />
				</svws-ui-input-wrapper>
			</div>
		</Variant>

		<Variant title="Required Length" id="Required Length">
			<div class="p-4 flex flex-col gap-10">
				<svws-ui-input-wrapper>
					<svws-ui-text-input type="text" model-value="zu lang" placeholder="Required Length" :max-len="5" @input="onInput" />
					<svws-ui-text-input type="text" model-value="zu kurz" placeholder="Required Length" :min-len="10" @input="onInput" />
				</svws-ui-input-wrapper>
				<svws-ui-input-wrapper>
					<svws-ui-text-input type="text" model-value="" placeholder="Required Length" :min-len="5" :max-len="10" @input="onInput" />
				</svws-ui-input-wrapper>
			</div>
		</Variant>

		<Variant title="Disabled" id="Disabled">
			<div class="p-4">
				<svws-ui-input-wrapper>
					<svws-ui-text-input type="text" model-value="" placeholder="Text Input Placeholder" disabled @input="onInput" />
					<svws-ui-text-input type="text" model-value="with value" placeholder="Text Input Placeholder" disabled @input="onInput" />
				</svws-ui-input-wrapper>
			</div>
		</Variant>

		<Variant title="Headless" id="Headless">
			<div class="bg-ui-neutral px-4 py-8 font-bold mb-1">
				<svws-ui-text-input type="text" model-value="" placeholder="Text Input Placeholder" headless @input="onInput" />
			</div>
			<div class="bg-ui-brand text-ui-onbrand px-4 py-8 font-bold">
				<svws-ui-text-input type="text" model-value="" placeholder="Text Input Placeholder" headless @input="onInput" />
			</div>
			<p class="mt-2 text-ui-secondary">
				Info: Die Hintergrundfarbe des Headless Inputs ist transparent und zeigt so immer die Hintergrundfarbe des Parent-Elements.<br>
				Mit <code class="bg-ui-neutral px-2 rounded-xs">bg-ui-xxxx text-ui-xxxx</code> in einem übergeordneten Element kann die Hintergrundfarbe und Textfarbe des Inputs geändert werden.
			</p>
		</Variant>

		<Variant title="Statistics" id="Statistics">
			<div class="p-4">
				<svws-ui-input-wrapper>
					<svws-ui-text-input type="text" model-value="" placeholder="Placeholder" statistics @input="onInput" />
					<svws-ui-text-input type="text" model-value="Statistic Value" placeholder="Placeholder" statistics @input="onInput" />
				</svws-ui-input-wrapper>
			</div>
		</Variant>

		<Variant title="Prefix (url)" id="Prefix">
			<div class="p-4">
				<svws-ui-input-wrapper>
					<svws-ui-text-input type="text" model-value="" placeholder="URL" url @input="onInput" />
					<svws-ui-text-input type="text" model-value="svws-nrw.de" placeholder="URL" url @input="onInput" />
				</svws-ui-input-wrapper>
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
