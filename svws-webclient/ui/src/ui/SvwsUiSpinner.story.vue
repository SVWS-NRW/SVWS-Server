
<template>
	<Story title="Spinner" id="svws-ui-spinner" icon="ri:loader-4-line" :layout="{type: 'grid', width: '45%'}">
		<Variant title="Spinning" id="spinning">
			<svws-ui-spinner spinning />
		</Variant>
		<Variant title="Button" id="laden">
			<svws-ui-button>
				Lädt
				<svws-ui-spinner spinning />
			</svws-ui-button>
		</Variant>
	</Story>
</template>
